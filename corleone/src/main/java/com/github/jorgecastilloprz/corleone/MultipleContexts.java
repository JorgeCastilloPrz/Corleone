package com.github.jorgecastilloprz.corleone;

/**
 * To keep method calls simple, we want to keep the syntax this way:
 *
 * Corleone.context("MyContext").dispatchJobs();
 * Corleone.context("MyContext").provideParam("MyParamQualifier", paramValue);
 * Corleone.allContexts(jobInstance).provideParam("MyParamQualifier", paramValue);
 *
 * But there is a problem with that. Developers are not going to get any instances of their jobs,
 * as the instantiation logic is automatized into the library. So the only way to call
 * Corleone.allContexts(jobInstance).whateverMethod() would be from inside of a @Job. That makes
 * sense if we are going to call provideParam() method, like:
 * Corleone.allContexts(this).provideParam("ParamQualifier", paramValue);
 * By that way, we provide params to following jobs on all the contexts from the current job.
 * But there is no real sense on doing:
 * Corleone.allContexts(this).dispatchJobs(); as we are inside a job.
 *
 * So this interface will be used to return a Corleone instance casted to it on the
 * Corleone.allContexts() method, getting the needed method occlusion for the user. Developers
 * will not really notice the new method dynamic type because of the way this library is used in
 * code. The calls will not really vary in syntax to him, but some of them will not be available
 * and the IDE will not suggest them.
 */
interface MultipleContexts {

  void provideParam(String qualifier, Object paramValue);

  void keepGoing();
}
