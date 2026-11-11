# Atelier 6 : Patrons de conception GoF et Principes SOLID — AutoLoc

Ce document fournit l'analyse architecturale complète, les réponses aux questions de l'atelier et la documentation des patrons de conception **GoF** (*Gang of Four*) implémentés dans le projet **AutoLoc**.

---

## 1. Partie 1 : Analyse d'AutoLoc v0

### 1.1 Contexte et classe problématique `AutoLocManager`
Dans la première version (v0), une classe unique `AutoLocManager` centralise l'ensemble du flux de location :
```java
public class AutoLocManager {
    public void louerVehicule(String typeReservation, int nombreJours, String clientEmail) {
        double prix = 0;
        if ("LongDuree".equals(typeReservation)) {
            prix = nombreJours * 120;
        } else if ("Weekend".equals(typeReservation)) {
            prix = nombreJours * 180;
        } else if ("Standard".equals(typeReservation)) {
            prix = nombreJours * 150;
        }
        System.out.println("Contrat créé");
        System.out.println("Prix total : " + prix);
        envoyerEmail(clientEmail);
        enregistrerPaiement();
        sauvegarderContrat();
    }
    // ...
}
```

---

### 1.2 Réponses aux questions d'analyse

#### Question 1 : Combien de responsabilités possède la classe `AutoLocManager` ?
**Réponse :**
La classe `AutoLocManager` possède **5 responsabilités distinctes** (anti-patron *God Class* ou *Blob*) :
1. **Calcul de la tarification métier** : application des barèmes tarifaires et des remises selon le type de réservation (`LongDuree`, `Weekend`, `Standard`).
2. **Gestion du cycle de vie du contrat** : création et formalisation du contrat de location.
3. **Gestion du paiement** : enregistrement et validation du paiement associé.
4. **Gestion de la notification client** : envoi des communications par Email (`envoyerEmail`) et par SMS (`envoyerSMS`).
5. **Persistance et présentation** : sauvegarde du contrat et affichage direct sur la console via `System.out.println`.

---

#### Question 2 : Quel principe SOLID n'est pas respecté ?
**Réponse :**
Le **SRP (Single Responsibility Principle - Principe de Responsabilité Unique)** est sévèrement violé.
* **Définition** : *« Une classe ne doit avoir qu'une et une seule raison de changer. »*
* **Constat dans `AutoLocManager`** : Cette classe possède au moins 5 raisons différentes de changer :
  * Si la politique tarifaire évolue (nouveaux tarifs, remises saisonnières),
  * Si le processus de contractualisation change,
  * Si le prestataire ou le mode de paiement change (ex. intégration Stripe / virement),
  * Si un nouveau canal de communication est introduit (SMS, WhatsApp, Push mobile),
  * Si la méthode de persistance évolue (remplacement de la console par une base de données MySQL).

---

#### Question 3 : Que se passe-t-il si AutoLoc ajoute un nouveau type de réservation (ex. `Pleinesaison`) ?
**Réponse :**
1. **Impact sur le code** :
   * Le développeur est contraint d'ouvrir le fichier source `AutoLocManager.java` et de modifier directement le corps de la méthode `louerVehicule`.
   * Il doit ajouter une nouvelle branche conditionnelle :
     ```java
     else if ("Pleinesaison".equals(typeReservation)) {
         prix = nombreJours * 220;
     }
     ```
2. **Risques induits** :
   * Risque élevé de **régression** sur les calculs existants.
   * Nécessité de recompiler et retester l'intégralité de la classe.
   * Augmentation continue de la complexité cyclomatique.

3. **Violation de principes SOLID** :
   * **OCP (Open/Closed Principle)** : *« Les entités logicielles doivent être ouvertes à l'extension, mais fermées à la modification. »*
     Ici, pour étendre le système avec un nouveau tarif, on est obligé de **modifier** le code existant au lieu de créer une nouvelle classe.
   * **DIP (Dependency Inversion Principle)** : La classe dépend directement de détails concrets et d'appels statiques console, sans passer par des abstractions (interfaces).

---

## 2. Architecture refactorée : Patrons de conception GoF

Pour résoudre ces problèmes, l'application a été refactorée en appliquant trois patrons de conception fondamentaux :

```
┌─────────────────────────────────────────────────────────────┐
│                     AutoLoc Architecture                    │
└─────────────────────────────────────────────────────────────┘

    [ ReservationService ]
             │
             ├── (déclenche) ──> [ ReservationSubject ] (Observer Pattern)
             │                           │
             │                           ├──> [ EmailNotificationObserver ]
             │                           └──> [ SmsNotificationObserver ]
             │
             └── (calcule) ──> [ TarificationStrategyFactory ] (Factory Pattern)
                                         │
                                         ├──> [ TarifStandardStrategy ]   (Strategy)
                                         ├──> [ TarifWeekendStrategy ]    (Strategy)
                                         └──> [ TarifLongueDureeStrategy ](Strategy)
```

---

### 2.1 Patron Strategy (`ITarificationStrategy`)
* **Package** : `tn.esprit.autoloc.service.strategy`
* **Objectif** : Encapsuler chaque algorithme de tarification dans une classe interchangeable implémentant `ITarificationStrategy`.
* **Classes concrètes** :
  * `TarifStandardStrategy` : tarif journalier brut ($\times 1.00$). Code : `STANDARD`.
  * `TarifWeekendStrategy` : application d'une remise de 10% (coefficient $0.90$). Code : `WEEKEND`.
  * `TarifLongueDureeStrategy` : application d'une remise de 20% (coefficient $0.80$) pour les séjours de 7 jours et plus. Code : `LONGUE_DUREE`.
* **Respect de l'OCP** : Pour ajouter le tarif `Pleinesaison`, il suffit d'ajouter une classe `TarifPleineSaisonStrategy implements ITarificationStrategy` annotée `@Component`, **sans modifier aucune ligne de code existante**.

---

### 2.2 Patron Factory (`TarificationStrategyFactory` & `NotificationFactory`)
* **Package** : `tn.esprit.autoloc.service.factory`
* **Objectif** : Découpler le code appelant de l'instanciation directe des stratégies et observateurs (`new TarifStandardStrategy()`).
* **`TarificationStrategyFactory`** :
  * Injecte automatiquement via Spring l'ensemble des beans implémentant `ITarificationStrategy` dans une `Map<String, ITarificationStrategy>`.
  * Méthode `resoudre(Reservation reservation)` :
    * Si $\text{durée} \ge 7\text{ jours}$ $\rightarrow$ `LONGUE_DUREE`
    * Si $\text{durée} \le 3\text{ jours}$ et débutant le week-end (vendredi, samedi, dimanche) $\rightarrow$ `WEEKEND`
    * Sinon $\rightarrow$ `STANDARD` (fallback par défaut).
* **`NotificationFactory`** :
  * Fournit l'observateur adéquat selon le canal demandé (`"EMAIL"` ou `"SMS"`).

---

### 2.3 Patron Observer (`ReservationSubject` & `IReservationObserver`)
* **Package** : `tn.esprit.autoloc.service.observer`
* **Objectif** : Découpler la gestion des réservations de la notification client et des traitements métier annexes (statistiques, audit).
* **Composants** :
  * `IReservationObserver` : Interface définissant `onReservationConfirmee(Reservation)` et `onReservationAnnulee(Reservation)`.
  * `ReservationSubject` : Maintient la liste des observateurs et propage les événements du cycle de vie.
  * `EmailNotificationObserver` : Génère le log formaté `[EMAIL] Confirmation envoyee...` ou `[EMAIL] Annulation notifiee...`.
  * `SmsNotificationObserver` : Génère le log formaté `[SMS] Confirmation envoyee...` ou `[SMS] Annulation notifiee...`.

---

## 3. Matrice des tests unitaires et d'intégration

| Classe de test | Méthode de test | Cas testé | Résultat attendu |
|---|---|---|---|
| **TarificationStrategyFactoryTest** | `doitRetournerLongueDureePour7JoursOuPlus` | Séjour du 2024-01-01 au 2024-01-08 (7 jours) | Stratégie `LONGUE_DUREE` |
| **TarificationStrategyFactoryTest** | `doitRetournerLongueDureePourExactement7Jours` | Séjour du 2024-05-01 au 2024-05-08 (borne 7 jours) | Stratégie `LONGUE_DUREE` |
| **TarificationStrategyFactoryTest** | `doitRetournerWeekendPourCourtSejourVendredi` | Séjour du 2024-03-08 au 2024-03-10 (2 jours, début vendredi) | Stratégie `WEEKEND` |
| **TarificationStrategyFactoryTest** | `doitRetournerStandardPourMoyenneDuree` | Séjour du 2024-03-04 au 2024-03-08 (4 jours, début lundi) | Stratégie `STANDARD` |
| **ReservationServiceTest** | `testCreerReservationEtNotification` | Création de réservation pour un client | Statut `CONFIRMEE` + logs `[EMAIL]` & `[SMS]` |
| **ReservationServiceTest** | `testAnnulerReservationEtNotification` | Annulation de réservation existante | Statut `ANNULEE` + logs `[EMAIL]` & `[SMS]` |
| **NotificationFactoryTest** | `doitRetournerEmailObserver` | Demande canal `"EMAIL"` | Instance `EmailNotificationObserver` |
| **NotificationFactoryTest** | `doitRetournerSmsObserver` | Demande canal `"SMS"` | Instance `SmsNotificationObserver` |
| **StrategyCalculationTest** | `testCalculStandard` | 4 jours à 100.00 DT/jour | 400.00 DT |
| **StrategyCalculationTest** | `testCalculWeekendAvecRemise` | 2 jours à 100.00 DT/jour (-10%) | 180.00 DT |
| **StrategyCalculationTest** | `testCalculLongueDureeAvecRemise` | 10 jours à 100.00 DT/jour (-20%) | 800.00 DT |

