Instructions
=========


1. Setup
--

- 1.1 Cloner le repository et se placer sur la branche `start`.
- 1.2 Importer le projet dans votre IDE préféré (de préférence un qui a le thème Darcula ;)).
- 1.3 Configurer un émulateur Android qui cible la version 17 de la plateforme `http://developer.android.com/tools/devices/managing-avds.html` et le démarrer.
- 1.4 Dans le fichier AndroidManifest.xml, ajouter la permission pour autoriser l'accès à Internet `<uses-permission android:name="android.permission.INTERNET" />`.


2. Récupération des données & affichage dans la liste
--

- 2.1 MainActivity (UI)
 - 2.1.1 Initialiser le membre ArrayAdapter avec un layout par défaut `android.R.layout.simple_list_item_1`
 - 2.1.2 Setter l'adapteur à la ListActivity
 - 2.1.3 (optionnel) Ajouter 2, 3 chaînes de caractères hard codées dans l'adapter et exécuter l'application, une fois l'application démarrée, vous devriez voir vos entrées dans la liste. Supprimer les entrées hard codées.
 - 2.1.4 Récupérer l'unique instance de la classe TwitterServiceHelper et la setter au membre mServiceHelper.
 - 2.1.5 Récupérer le bouton définit dans le main layout avec l'id `R.id.refresh` et lui ajouter un OnClickListener qui invoque la méthode `refreshTweets` de l'activité.
 - 2.1.6 Implémenter la méthode `refreshTweets` de l'activité en affichant une notification Toast pour prévenir du rafraîchisement des données et en invoquant la méthode éponyme du TwitterServiceHelper.
 - 2.1.7 Pour être notifié des nouveaux résultats, il va falloir ajouter l'activité en tant que listener auprès du TwitterServiceHelper 
     - Implémenter l'interface TwitterEventListener au sein de l'activité. Pour la méthode onNewTweets, supprimer le contenu déjà présent dans l'adapter et lui setter les nouveaux tweets passées en paramètre. Pour la méthode onError, afficher simplement un notification Toast pour prévenir l'utilisateur.
     - Ajouter l'activité en tant que listener auprès du TwitterServiceHelper dans la phase onResume de son cycle de vie.
     - Retirer l'activité en tant que listener auprès du TwitterServiceHelper dans la phase onPause de son cycle de vie.

- 2.2 TwitterServiceHelper (Façade qui expose une API haut niveau à l'activité et qui est en charge d'invoquer le traitement asynchrone de récupération des données et de notifier ses listeners des résultats en retour)
 - 2.2.1 Implémenter la méthode refreshTweets qui invoque le service TwitterService en lui passant un intent qui a pour paramètre un ResultReceiver, qui, en fonction du code HTTP retour, invoque la méthode onNewTweets de ses listeners (code HTTP 200) ou la méthode onError.
 
- 2.3 TwitterService
 - 2.3.1 Récupérer le ResultReceiver au sein de l'intent.
 - 2.3.2 Implémenter la méthode prepareRequest qui créée une requête HTTP GET, poitant sur `http://search.twitter.com/search.json` et ayant comme paramètre `q=android`
 - 2.3.3 Exécuter la requête HTTP 
 - 2.3.4 Invoquer la méthode processResponse et l'implémenter pour notifier les résultats via le ResultReceiver.


Lancer l'application, cliquer sur le bouton "refresh tweets" : Magie ! (on l'espère)
Changer l'orientation de l'écran (CTRL + F12 sur l'émulateur), horreur ! les données ont disparu ! 


3. Caching des données
--

Pour la persistence, nous allons introduire 2 nouveaux objets : 
 - TweetsLoader, qui va être chargé à chaque redémarrage d'activité et à chaque notification de nouveaux résultats d'aller chercher les derniers tweets en base. Un callback sera implémenté dans l'activité pour pouvoir setter ces résultats à l'adapteur.
 - DatabaseHelper, un helper qui permet d'obtenir une base en lecture ou écriture, le tout avec un support transactionnel, et qui va nous permettre de supprimer les anciens tweets et d'insérer les nouveaux au sein du service, et aussi de lire les nouvelles entrées pour les afficher dans la ListView de notre activité.

 - 3.1 Jetter un oeil la classe DatabaseHelper.
 - 3.2 Implémenter la méthode `loadInBackground` du TweetsLoader qui va, de manière asynchrone, récupérer les entrées de la table tweets via un cursor. La classe SQLiteQueryBuilder peut aider pour définir la table et la requête. Jetter un oeil aux différentes méthodes du cycle de vie du TweetsLoader.
 - 3.3 Au sein de l'activité, implémenter l'interface `LoaderManager.LoaderCallbacks<Cursor>`. Retourner une nouvelle instance du loader au sein de la méthode onCreateLoader. Au sein de la méthode onLoadFinished, si le cursor est null ou ne contient pas d'éléments, invoquer la méthode refreshTweets, sinon récupérer les tweets et les setter à l'adapter. Inutile d'implémenter la méthode onLoaderReset.
 - 3.4 Dans la méthode onCreate de l'activité, initialiser le loader à l'aide du LoaderManager.
 - 3.5 Dans la méthode onNewTweets de l'activité, relancer le loader pour forcer la récupération des données en base.
 - 3.6 Implémenter la méthode persistTweets du TwitterService, qui va obtenir une DB en écriture, puis, au sein d'une transaction, supprimer les anciens tweets et insérer les nouveaux.

Démarrer l'application et rafraîchir les résultats. Changer l'orientation de l'écran (CTRL + F12 sur l'émulateur), l'activité est récréé et le loader, dont le loader a déjà été peuplé, renvoie directement le cursor à l'acitvité pour remplir l'adapter. Sortir de l'application, y retourner, les derniers résultats récupérés devraient être affichés.
    
*Have Fun !*