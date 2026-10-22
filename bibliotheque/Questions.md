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
“prix” : 12.50,
“anneePublication” : 1862,
“stock” : 5,
“auteurId” : 1
}
