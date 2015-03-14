Corleone
========

<img alt="Corleone Java APT" src="./art/corleone.png" />

Java annotation processor library used to dispatch and concatenate background tasks in a decoupled way through the following annotations:

Usage
=====

The use of the library is pretty simple. First of all, you will need to know the available annotations.

Annotations
-----------

* ```@Job```: Used on top of your background task classes, it can contain multiple annotations of type @Rule.
* ```@Rule```: Every job task can have multiple contexts defined to allow different instances of the same job running on different contexts.
Each context ```@Rule``` has an optional field for declaring a previous job task (for job concatenation).
* ```@Param```: Used on job class attribute fields to allow param injection through autogenerated param binders. As the responsibily of building job instances
will reside on the library, job class constructors will not be allowed. Params will satisfy their corresponding runtime values when the job gets instantiated by Corleone.
Keep reading to know how to provide them.
* ```@Execution```: Used to annotate a job method. ```@Execution``` method will be the one autoexecuted to do the job.

Jobs
----

Corleone loves to delegate Jobs on his beloved family members to forget worries. Jobs will be the background tasks, and their syntax will be like:
```java
@Job({
    @Rule(context = "ObtainGames", previousJob = CheckNetworkConnection.class)
})
public class GetGamesFromService {

    @Param("RestRepo") GameRepository restGameRepository;
    @Param("PageNum") int pageNumber;
    @Param("RestCallback") Callback callback;
    @Param("MainThread") MainThread mainThread;

    @Execution
    public void run() {
        try {
            List<Game> games = gameRepository.obtainGamesByPage(pageNumber);
            notifyGamesLoaded(games);
        }
        catch (ObtainGamesException exception) {
            notifyPetitionError(exception.getMessage());
        }
    }

    private void notifyGamesLoaded(final List<Game> games) {
      mainThread.post(new Runnable() {
         @Override
         public void run() {
            callback.onGamePageLoaded(games);
         }
      });
      Corleone.with("ObtainGames").provideParam("games", games);
      Corleone.keepGoing();
    }

    private void notifyPetitionError(final String message) {
      mainThread.post(new Runnable() {
         @Override
         public void run() {
            callback.onGettingGamesError(message);
         }
      });
    }
}
```

Param providing
---------------

You can provide `@Job` parameters by two different ways. The first one is into the `@Job` dispatch call arguments.
```java
JobParams jobParams = new JobParams()
		.append("ConnectivityManager", connectivityManager);
		.append("RestRepo", restGameRepository)
		.append("PersistenceRepo", persistenceGameRepository)
 		.append("PageNum", pageNumber)
		.append("RestCallback", getRestGameQueryCallback())
		.append("PersistenceCallback", getPersistenceGameQueryCallback())
		.append("MainThread", mainThread);

	Corleone.with("ObtainGames").dispatchJobs(jobParams);
```

Dependencies
------------

* [AutoService][dependencies-1]: One of the providers of Google auto library. A metadata/config generator to avoid typical ServiceLoader manual setup.
* [JavaPoet][dependencies-2]: A Square's java source file generation library. It's the successor of JavaWriter.

Testing
-------

* [Compile-testing][testing-libs-1]: Google testing framework to allow testing java sources @ javac compile time.
* [Truth][testing-libs-2]: Google library to "humanize" language of JUnit testing assertions.
* [JUnit][testing-libs-3]: Base for all the project unit tests.

Developed By
------------
* Jorge Castillo Pérez - <jorge.castillo.prz@gmail.com>

<a href="https://www.linkedin.com/in/jorgecastilloprz">
  <img alt="Add me to Linkedin" src="https://github.com/JorgeCastilloPrz/EasyMVP/blob/master/art/linkedin.png" />
</a>

License
-------

    Copyright 2015 Jorge Castillo Pérez

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

 [dependencies-1]: https://github.com/google/auto/tree/master/service
 [dependencies-2]: https://github.com/square/javapoet
 [testing-libs-1]: https://github.com/google/compile-testing
 [testing-libs-2]: https://github.com/google/truth
 [testing-libs-3]: http://junit.org/