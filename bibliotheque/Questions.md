Q1

BASE URL : /api/v1/livres
Element d’id 3 :  /api/v1/livres/3

BASE URL : /api/v1/auteurs
Element d’id 3 :  /api/v1/auteurs/3

BASE URL : /api/v1/emprunts
Element d’id 3 :  /api/v1/emprunts/3

Q2

4 règles :
pas de verbe dans l’url
pluriel
minuscules
pas de slash final
pas d’extension de fichier

Q3

si 2 post identiques à la suite : premier, 201 Created, deuxième, 409 Conflict
si 2 delete identiques : premier 204 No content, deuxième, 404 Not found


Q4

POST /auteurs avec un corps valide  : Code 201 Created
DELETE /livres/2 (le livre existe)  : Code 204 No Content
GET /livres/999  : Code 404 Not Found
POST /livres avec un corps valide mais "auteurId": 999  : Code 400 Bad Request
POST /livres avec l'ISBN du livre 1 : Code 400 Bad Request
POST /emprunts pour le livre 8 (« Le Hobbit »)  : Code 200 OK
POST /auteurs avec "nom": ""  : Code 400 Bad Request
DELETE /auteurs/1 (Victor Hugo)  : Code 204 No Content ( si tous les livres de victor hugo ont été supprimés ), sinon 400 Bad Request


Q5

GET /livres/1

{
“titre” : “Les Misérables”,
“isbn” : “978-2-07-040850-4 ”,
“prix” : 0.0,
“anneePublication” : 15,
“stock” : 0,
“auteurId” : 55
}


Q6

1. Écrivez l'URL complète qui demande la deuxième page des livres, avec 3 livres par page, triés par prix décroissant.

URL :  /api/v1/livres?page=2&size=3&sort=price,desc

2. Avec les 8 livres initiaux et 3 livres par page, quelles sont les valeurs de page.totalElements et de page.totalPages ?
   page.totalElements : 3,  page.totalPages : 3


3. Donnez deux raisons de paginer une collection plutôt que de tout renvoyer.
   Si il y a 1259038 éléments dans la collection, cela devient dangereux pour le service en le déstabilisant, en plus de compliquer la lecture de la réponse


Q7

1. Que signifie la contrainte REST « sans état » (stateless) ?
   Le serveur ne garde pas de trace des requêtes

2. Le corps de POST /emprunts contient le nom et l'email de l'emprunteur. Expliquez le lien avec la contrainte « sans état ».
   Les données qui doivent être retenues sont incluses dans la requête


Q8

1. Pourquoi EmpruntRequest ne contient-il ni id, ni dates, ni rendu ?
   EmpruntRequest est un dto réponse, il ne renvoie que les champs explicitement choisis

2. Pourquoi EmpruntResponse ne contient-il pas emailEmprunteur ?
   Car emailEmprunteur est jugé comme privé, est ne doit pas être accessible

3. Donnez deux problèmes que poserait le fait de renvoyer directement l'entité Emprunt au client.
   Avoir accès à l’id et aux champs confidentiels, et avoir accès aux setters


Q9

1. Commande curl pour GET /auteurs/1
   curl.exe -i https://localhost:8080/api/v1/auteurs/1

2. En-tête précisant que le corps est du JSON :
   Content-Type : application/json

3. Commande curl pour POST l'auteur
   curl.exe -X POST -H "Content-Type: application/json" -d " {\" nom\" :\" Verne\" ,\" prenom\" :\" Jules\" ,\" nationalite\" :\" Française\" }"  https://localhost:8080/api/v1/auteurs
