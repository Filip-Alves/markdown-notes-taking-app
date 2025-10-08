# Application de Prise de Notes Markdown

Une API RESTful simple, développée avec Java et Spring Boot, permettant de gérer des notes au format Markdown. Elle inclut la gestion CRUD complète des notes, la conversion Markdown vers HTML, la vérification grammaticale et l'upload de fichiers.

## Technologies Utilisées

*   **Langage :** Java 21
*   **Framework :** Spring Boot
*   **Accès aux données :** Spring Data JPA (Hibernate)
*   **Base de données :** PostgreSQL
*   **Build :** Maven
*   **Bibliothèques clés :**
    *   Lombok (pour réduire le code boilerplate)
    *   Flexmark (pour la conversion Markdown -> HTML)
    *   LanguageTool (pour la vérification de la grammaire)

## Prérequis

*   JDK 21 ou supérieur
*   Maven 3.6+
*   PostgreSQL

## Installation & Lancement

1.  **Clonez le dépôt**
    ```bash
    git clone https://github.com/votre-pseudo/markdown-notes.git
    cd markdown-notes
    ```

2.  **Créez la base de données**
    Connectez-vous à PostgreSQL et exécutez la commande suivante :
    ```sql
    CREATE DATABASE markdown_notes_db;
    ```

3.  **Configurez la connexion à la base de données**
    Ouvrez le fichier `src/main/resources/application.properties` et modifiez les lignes suivantes avec vos propres informations :
    ```properties
    spring.datasource.url=jdbc:postgresql://localhost:5432/markdown_notes_db
    spring.datasource.username=votre_utilisateur_postgres
    spring.datasource.password=votre_mot_de_passe_postgres
    ```

4.  **Lancez l'application**
    ```bash
    mvn spring-boot:run
    ```    
    L'API sera accessible à l'adresse `http://localhost:8080`.

## Points d'API Disponibles

### Gestion des Notes (`/api/notes`)

---

#### `POST /api/notes`
Crée une nouvelle note à partir d'un corps JSON.

*   **Corps (JSON) :**
    ```json
    {
        "title": "Titre de ma note",
        "content": "## Contenu de la note\n\n- Point 1\n- Point 2"
    }
    ```
*   **Réponse (Succès) :** `201 Created` avec l'objet de la note créée.

---

#### `POST /api/notes/upload`
Crée une nouvelle note à partir d'un fichier Markdown (`.md`).

*   **Corps (form-data) :**
    *   `title` (text) : Titre de la note.
    *   `file` (file) : Le fichier Markdown à uploader.
*   **Réponse (Succès) :** `201 Created` avec l'objet de la note créée.

---

#### `GET /api/notes`
Récupère la liste de toutes les notes.

*   **Réponse (Succès) :** `200 OK` avec un tableau d'objets `Note`.

---

#### `GET /api/notes/{id}`
Récupère une note par son identifiant.

*   **Réponse (Succès) :** `200 OK` avec l'objet `Note` correspondant.
*   **Réponse (Erreur) :** `404 Not Found` si l'ID n'existe pas.

---

#### `GET /api/notes/{id}/html`
Récupère le contenu d'une note rendu au format HTML.

*   **Réponse (Succès) :** `200 OK` avec le contenu en `text/html`.
*   **Réponse (Erreur) :** `404 Not Found`.

---

#### `PUT /api/notes/{id}`
Met à jour une note existante.

*   **Corps (JSON) :**
    ```json
    {
        "title": "Nouveau titre",
        "content": "Nouveau contenu."
    }
    ```
*   **Réponse (Succès) :** `200 OK` avec l'objet `Note` mis à jour.
*   **Réponse (Erreur) :** `404 Not Found`.

---

#### `DELETE /api/notes/{id}`
Supprime une note par son identifiant.

*   **Réponse (Succès) :** `204 No Content`.
*   **Réponse (Erreur) :** `404 Not Found`.

---

### Outils de Grammaire (`/api/grammar`)

---

#### `POST /api/grammar/check`
Vérifie la grammaire d'un texte fourni.

*   **Corps (JSON) :**
    ```json
    {
        "text": "Ceci est un texte avec un erreur."
    }
    ```
*   **Réponse (Succès) :** `200 OK` avec un tableau d'erreurs grammaticales potentielles.