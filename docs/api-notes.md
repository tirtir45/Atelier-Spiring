# Atelier 5 : Spring MVC et SpringDoc — Documentation et Notes d'API

## A. Endpoints

| Contrôleur | Verbe HTTP et Route | Service appelé | Code de succès | Codes d'erreur |
|---|---|---|---|---|
| **ContratController** | `GET /api/contrats` | `contratService.findAll()` | 200 OK | — |
| **ContratController** | `GET /api/contrats/{id}` | `contratService.findById(id)` | 200 OK | 404 Not Found |
| **ContratController** | `POST /api/contrats` | `contratService.create(contrat)` | 201 Created | 400 Bad Request |
| **ContratController** | `PUT /api/contrats/{id}` | `contratService.update(id, contrat)` | 200 OK | 400 Bad Request, 404 Not Found |
| **ContratController** | `DELETE /api/contrats/{id}` | `contratService.deleteById(id)` | 204 No Content | 404 Not Found |
| **PaiementController** | `GET /api/paiements` | `paiementService.findAll()` | 200 OK | — |
| **PaiementController** | `GET /api/paiements/{id}` | `paiementService.findById(id)` | 200 OK | 404 Not Found |
| **PaiementController** | `POST /api/paiements` | *(Non implémenté - lecture seule)* | — | 405 Method Not Allowed |
| **AgenceController** | `GET /api/agences` | `agenceService.findAll()` | 200 OK | — |
| **AgenceController** | `GET /api/agences/{id}` | `agenceService.findById(id)` | 200 OK | 404 Not Found |
| **AgenceController** | `POST /api/agences` | `agenceService.create(agence)` | 201 Created | 400 Bad Request |
| **AgenceController** | `PUT /api/agences/{id}` | `agenceService.update(id, agence)` | 200 OK | 400 Bad Request, 404 Not Found |
| **AgenceController** | `DELETE /api/agences/{id}` | `agenceService.deleteById(id)` | 204 No Content | 404 Not Found |
| **EmployeController** | `GET /api/employes` | `employeService.findAll()` | 200 OK | — |
| **EmployeController** | `GET /api/employes/{id}` | `employeService.findById(id)` | 200 OK | 404 Not Found |
| **EmployeController** | `POST /api/employes` | `employeService.create(employe)` | 201 Created | 400 Bad Request |
| **EmployeController** | `PUT /api/employes/{id}` | `employeService.update(id, employe)` | 200 OK | 400 Bad Request, 404 Not Found |
| **EmployeController** | `DELETE /api/employes/{id}` | `employeService.deleteById(id)` | 204 No Content | 404 Not Found |
| **VehiculeController** | `GET /api/vehicules` | `vehiculeService.findAll()` | 200 OK | — |
| **VehiculeController** | `GET /api/vehicules/{id}` | `vehiculeService.findById(id)` | 200 OK | 404 Not Found |
| **VehiculeController** | `POST /api/vehicules` | `vehiculeService.create(vehicule)` | 201 Created | 400 Bad Request |
| **VehiculeController** | `PUT /api/vehicules/{id}` | `vehiculeService.update(id, vehicule)` | 200 OK | 400 Bad Request, 404 Not Found |
| **VehiculeController** | `DELETE /api/vehicules/{id}` | `vehiculeService.deleteById(id)` | 204 No Content | 404 Not Found |
| **EquipementController** | `GET /api/equipements` | `equipementService.findAll()` | 200 OK | — |
| **EquipementController** | `GET /api/equipements/{id}` | `equipementService.findById(id)` | 200 OK | 404 Not Found |
| **EquipementController** | `POST /api/equipements` | `equipementService.create(equipement)` | 201 Created | 400 Bad Request |
| **EquipementController** | `PUT /api/equipements/{id}` | `equipementService.update(id, equipement)` | 200 OK | 400 Bad Request, 404 Not Found |
| **EquipementController** | `DELETE /api/equipements/{id}` | `equipementService.deleteById(id)` | 204 No Content | 404 Not Found |
| **ClientController** | `GET /api/clients` | `clientService.findAll()` | 200 OK | — |
| **ClientController** | `GET /api/clients/{id}` | `clientService.findById(id)` | 200 OK | 404 Not Found |
| **ClientController** | `POST /api/clients` | `clientService.create(client)` | 201 Created | 400 Bad Request |
| **ClientController** | `PUT /api/clients/{id}` | `clientService.update(id, client)` | 200 OK | 400 Bad Request, 404 Not Found |
| **ClientController** | `DELETE /api/clients/{id}` | `clientService.deleteById(id)` | 204 No Content | 404 Not Found |
| **ReservationController** | `GET /api/reservations` | `reservationService.findAll()` | 200 OK | — |
| **ReservationController** | `GET /api/reservations/{id}` | `reservationService.findById(id)` | 200 OK | 404 Not Found |
| **ReservationController** | `POST /api/reservations` | `reservationService.create(reservation)` | 201 Created | 400 Bad Request |
| **ReservationController** | `PUT /api/reservations/{id}` | `reservationService.update(id, reservation)` | 200 OK | 400 Bad Request, 404 Not Found |
| **ReservationController** | `DELETE /api/reservations/{id}` | `reservationService.deleteById(id)` | 204 No Content | 404 Not Found |
| **MaintenanceController** | `GET /api/maintenances` | `maintenanceService.findAll()` | 200 OK | — |
| **MaintenanceController** | `GET /api/maintenances/{id}` | `maintenanceService.findById(id)` | 200 OK | 404 Not Found |
| **MaintenanceController** | `POST /api/maintenances` | `maintenanceService.create(maintenance)` | 201 Created | 400 Bad Request |
| **MaintenanceController** | `PUT /api/maintenances/{id}` | `maintenanceService.update(id, maintenance)` | 200 OK | 400 Bad Request, 404 Not Found |
| **MaintenanceController** | `DELETE /api/maintenances/{id}` | `maintenanceService.deleteById(id)` | 204 No Content | 404 Not Found |

---

## B. Annotations Jackson

### 1. Associations, annotations retenues et justifications

| Association | Annotation retenue | Justification |
|---|---|---|
| **Contrat $\leftrightarrow$ Paiement** (Composition parent-enfant avec `cascade = ALL`) | `@JsonManagedReference("contrat-paiements")` sur `Contrat.paiements` et `@JsonBackReference("contrat-paiements")` sur `Paiement.contrat` | Permet d'inclure les paiements lors de la sérialisation d'un contrat (`GET /contrats`), de créer les paiements en cascade dans le corps JSON du parent (`POST /contrats`), tout en injectant automatiquement la référence au contrat parent lors de la désérialisation sans provoquer de boucle infinie (`StackOverflowError`). |
| **Agence $\leftrightarrow$ Employe** | `@JsonIgnore` sur `Agence.employes` | L'employé référence son agence (`@ManyToOne`). L'agence n'a pas besoin de sérialiser l'ensemble de ses employés dans son JSON, ce qui coupe la boucle bidirectionnelle. |
| **Agence $\leftrightarrow$ Vehicule** | `@JsonIgnore` sur `Agence.vehicules` | Le véhicule référence son agence d'attache. La liste des véhicules est ignorée dans la réponse JSON de l'agence pour éviter la récursion cyclique. |
| **Client $\leftrightarrow$ Reservation** | `@JsonIgnore` sur `Client.reservations` | Une réservation référence son client. Lors de la création d'une réservation, le client est fourni par son ID (`{"client": {"idClient": 1}}`). Si on mettait `@JsonBackReference` sur `Reservation.client`, le client ne serait jamais lu lors de la désérialisation du POST, créant une réservation orpheline. `@JsonIgnore` côté client évite ce piège. |
| **Vehicule $\leftrightarrow$ Reservation** | `@JsonIgnore` sur `Vehicule.reservations` | Même justification : le véhicule est référencé par la réservation, la liste des réservations du véhicule n'est pas réémise pour briser le cycle. |
| **Vehicule $\leftrightarrow$ Maintenance** | `@JsonIgnore` sur `Vehicule.maintenances` | La maintenance référence son véhicule (`@ManyToOne`). La collection de maintenances de l'entité véhicule est ignorée. |
| **Vehicule $\leftrightarrow$ Equipement** (ManyToMany) | `@JsonIgnore` sur `Equipement.vehicules` | Un véhicule expose sa liste d'équipements, mais l'équipement masque la liste de véhicules équipés pour couper le graphe cyclique. |
| **Contrat $\leftrightarrow$ Reservation** (OneToOne) | `@JsonIgnore` sur `Contrat.reservation` | Permet d'accéder au contrat sans réinjecter la réservation qui pointe à nouveau sur le contrat. |

### 2. Différence entre `@JsonBackReference` et `@JsonIgnore`

* **`@JsonIgnore`** :
  Exclut **totalement** la propriété annotée de la sérialisation (Java $\rightarrow$ JSON) et de la désérialisation (JSON $\rightarrow$ Java). Le champ est purement et simplement ignoré par Jackson dans les deux sens.
* **`@JsonBackReference`** :
  Fonctionne en binôme indissociable avec `@JsonManagedReference`. La propriété est omise lors de la **sérialisation** (pour rompre la boucle), mais lors de la **désérialisation**, Jackson reconstitue automatiquement le lien bidirectionnel en affectant l'instance du parent annoté par `@JsonManagedReference` au champ marqué par `@JsonBackReference`.

---

## C. Postman et Swagger

### Rôles complémentaires et apports respectifs

| Outil | Rôle principal | Apport concret dans l'atelier |
|---|---|---|
| **Postman** | Client HTTP de test, d'automatisation et de rejouabilité de scénarios d'intégration. | Permet d'écrire des assertions (`pm.test(...)`), de sauvegarder dynamiquement des identifiants générés dans des variables d'environnement (`pm.environment.set("contratId", ...)`), et de valider les scénarios en cascade (création d'un contrat avec 2 paiements imbriqués, vérification immédiate dans `GET /api/paiements`). |
| **Swagger UI (SpringDoc)** | Documentation interactive et contrat vivant de l'API REST. | Génère automatiquement la spécification OpenAPI v3 (`/api/v3/api-docs`) à partir du code source. Permet à un développeur ou à un intégrateur de visualiser l'ensemble des schémas d'entités, les routes disponibles, d'exécuter des requêtes d'essai (« Try it out »), et d'avoir un contrat toujours synchronisé avec le code sans documentation externe obsolète. |

**Exemple concret rencontré :**
Lors du test du cycle de vie d'un Contrat avec ses Paiements :
* Dans **Postman**, un script `After response` a extrait `pm.response.json().idContrat` pour alimenter la variable `{{contratId}}`, permettant d'enchaîner immédiatement avec la requête de modification `PUT /api/contrats/{{contratId}}` puis de suppression `DELETE /api/contrats/{{contratId}}`.
* Dans **Swagger UI**, la visualisation du schéma `Contrat` a permis de vérifier instantanément que `paiements` était bien documenté comme une liste d'objets `Paiement` sans le champ cyclique `contrat`.

---

## D. Anomalies SonarQube for IDE

| Règle SonarQube | Explication | Correction appliquée |
|---|---|---|
| **java:S3740 / java:S1452** *(Avoid using raw types or excessive exposure of persistent entities)* | **Exposition directe des entités JPA dans la couche contrôleur REST** : exposer directement les entités de domaine (`Contrat`, `Vehicule`, etc.) lie le modèle de données de la base à l'API publique, expose des données techniques (champs internes, proxys Hibernate LAZY provoquant `ByteBuddyInterceptor`), et ouvre un risque d'injection de masse (*mass assignment*). | **Solution transitoire Atelier 5** : application de `@JsonIgnore` et configuration de `spring.jackson.serialization.fail-on-empty-beans=false`. **Solution pérenne (Chapitre 10)** : introduction des objets DTO (*Data Transfer Objects*) et de mappeurs (MapStruct / ModelMapper). |
| **java:S6813** *(Field injection should not be used)* | **Injection de dépendance par champ (`@Autowired private I...Service`)** : l'injection par champ rend les classes difficiles à tester unitairement sans conteneur Spring, empêche de rendre le champ `final`, et favorise le couplage fort. | Utilisation systématique de champs `private final` combinés à l'annotation Lombok `@RequiredArgsConstructor` pour assurer une injection par constructeur propre et immuable. |
| **java:S1128** *(Unused imports should be removed)* | **Imports inutilisés ou superflus** dans les contrôleurs et entités (ex. imports de classes déplacées ou annotations non exploitées). | Nettoyage des imports inutiles dans l'ensemble des contrôleurs du package `tn.esprit.autoloc.web.controller`. |

---

## E. Questions de compréhension

### 1. Quel composant reçoit toutes les requêtes, et comment retrouve-t-il la méthode du contrôleur à appeler ?
Le **`DispatcherServlet`** (Front Controller de Spring MVC) reçoit toutes les requêtes HTTP entrantes. Il interroge ensuite le composant **`HandlerMapping`** (notamment `RequestMappingHandlerMapping`), qui inspecte l'URL demandée, la méthode HTTP (GET, POST, PUT, DELETE), les en-têtes et le type de contenu pour identifier la méthode du `@RestController` associée. Une fois la méthode trouvée, il délègue l'exécution via un `HandlerAdapter`.

### 2. Pourquoi `ContratController` dépend-il de `IContratService` et non de `ContratRepository` ni de `ContratServiceImpl` ?
* **Non-dépendance à `ContratRepository`** : Respect du principe de séparation des responsabilités et de l'architecture en couches. Le contrôleur s'occupe de la gestion HTTP (désérialisation, validation, codes de retour) et délègue l'ensemble de la logique métier (règles de gestion, validation d'identifiant, transactions) à la couche service.
* **Non-dépendance à `ContratServiceImpl`** : Respect du principe d'inversion des dépendances (DIP des principes SOLID). Dépendre de l'interface `IContratService` offre un découplage fort, facilite le remplacement d'implémentation et autorise l'utilisation aisée de bouchons (*mocks*) lors des tests.

### 3. Pourquoi un POST renvoie-t-il 201 et un DELETE 204 ? Pourquoi une ressource absente donne-t-elle 404 et non 500 ?
* **`POST -> 201 Created`** : Indique que la requête a réussi et qu'une nouvelle ressource a été créée sur le serveur avec son URI/identifiant propre.
* **`DELETE -> 204 No Content`** : Indique que l'opération de suppression a été accomplie avec succès et que le corps de la réponse ne contient aucune donnée.
* **`404 Not Found vs 500 Internal Server Error`** : Un code `4xx` désigne une erreur imputable au client (ici, demander une ressource dont l'identifiant n'existe pas en base). Renvoyer une erreur `500` laisserait croire à un bug applicatif interne ou à un crash non géré du serveur. L'exception `ResourceNotFoundException` est interceptée par le `GlobalExceptionHandler` pour traduire légitimement ce cas métier en `404`.

### 4. Pourquoi `PaiementController` n'expose-t-il que des GET ? Quelle réponse reçoit un POST `/api/paiements` ?
* **Raison** : Dans les règles métier du domaine AutoLoc, un `Paiement` n'a pas d'existence autonome : il est en composition stricte au sein d'un `Contrat` avec `cascade = CascadeType.ALL`. La création et la modification de paiements transitent exclusivement par le contrat qui les porte.
* **Réponse à un POST `/api/paiements`** : Spring MVC renvoie un code **`405 Method Not Allowed`**, car aucune méthode du contrôleur n'est associée à la méthode HTTP POST pour cette route.

### 5. Quels risques présente l'exposition directe des entités JPA ? Pourquoi `Contrat.paiements` utilise-t-il `@JsonManagedReference` alors que `Agence.vehicules` utilise `@JsonIgnore` ?
* **Risques de l'exposition directe des entités JPA** :
  1. *Couplage fort API / Schéma BDD* : Toute modification des colonnes de la base impacte directement les clients de l'API.
  2. *Fuite d'informations sensibles ou internes* : Exposition involontaire de données d'audit, de mots de passe ou de clés techniques.
  3. *Problèmes de sérialisation et performance* : Risque de récursions infinies (`StackOverflowError`) et déclenchement intempestif de requêtes SQL secondaires (*Lazy loading* / problème N+1 / proxies Hibernate `ByteBuddyInterceptor`).
  4. *Sur-assignation (Mass Assignment)* : Un utilisateur malveillant peut injecter des champs internes (ex. changer un rôle ou un identifiant) directement dans le corps JSON.
* **`@JsonManagedReference` sur `Contrat.paiements` vs `@JsonIgnore` sur `Agence.vehicules`** :
  * `Contrat ↔ Paiement` est une **composition forte** : un contrat porte la responsabilité de ses paiements dans son propre cycle de vie (création conjointe, affichage complet du contrat et de ses échéances). `@JsonManagedReference` permet d'exposer la liste des paiements tout en reconstruisant le lien inverse à la désérialisation.
  * `Agence ↔ Vehicule` est une **agrégation simple** : la flotte de véhicules est indépendante de l'agence. Sérialiser tous les véhicules d'une agence alourdirait inutilement la réponse JSON et risquerait de charger un volume excessif d'entités en base. Les véhicules possèdent leur propre endpoint dédié (`/api/vehicules`). `@JsonIgnore` élimine donc ce fardeau de manière étanche.
