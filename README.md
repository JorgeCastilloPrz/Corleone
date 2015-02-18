Corleone [![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-EasyMVP-brightgreen.svg?style=flat)](https://android-arsenal.com/details/3/1515)
===========================================================================================================================================================
Java annotation processor library created to generate a simplified java threadpool execution logic during compile time.

* ```@Dispatcher``` annotated method will be used by the user to initiate the execution. The processor will generate and inject the code into the last line of the method to start the execution by thread pool executor.
* ```@Execution``` annotation will be on top of the method user wants to set the use case logic into. The code into the annotated method will be the code executed by thread pool generated instance.
* ```@MainThread``` flagged methods will run its logic into the Android main looper thread. It will be often used to dispatch callback methods to allow the app interface to react. 

Testing
-------
The project has been created by using TDD. Following libraries are being used:

* ```Compile-testing```: Google testing framework to allow testing java sources @ javac compile time.
* ```Truth```: Google library to "humanize" language of JUnit testing assertions.
* ```jUnit```: Base for all the project unit tests.

Developed By
------------
* Jorge Castillo Pérez - <jorge.castillo.prz@gmail.com>

<a href="https://www.linkedin.com/in/jorgecastilloprz">
  <img alt="Add me to Linkedin" src="./art/linkedin.png" />
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

