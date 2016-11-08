# Overview

The Rich Relevance SDK is a native Java interface to the Rich Relevance API v1.0. 

## Directory Structure

Below is a brief overview of the directory structure for this project:

 * testApp: A basic project that demonstrates a subset of the SDK functionality.
 * richrelevance/src/main: The main SDK project.
 * richrelevance/src/androidTest: Test cases for the SDK project.

## Build, Run, Test, & Package
 
The SDK project requires no external dependencies other than the Android SDK. The project is configured to be built with Gradle. As such, you can simply open the root and run ```./gradlew assembleRelease``` to build the project.

### Build Configurations

The top-level project has several tasks, which are briefly described below:

 * ```assembleRelease``` Builds the SDK and exports an .aar.
 * ```connectedCheck``` Builds the SDK and runs all unit and integration tests. (Requires a connected device)
 * ```releaseJavadoc``` Builds the SDK and exports HTML Javadoc files.
 * ```releaseJavadocJar``` Builds the SDK and exports a Javadoc jar.

### Artifacts

Artifacts are output to the following locations:

 * Library archive (.aar) - richrelevance/build/outputs/aar/richrelevance-release.aar
 * Javadoc HTML - richrelevance/build/docs/javadoc
 * Javadoc JAR - richrelevance/build/libs/richrelevance-javadoc.jar

### Dependencies

As stated, Gradle is used for building and managing dependencies within the SDK project. Since there are currently no external dependencies, Gradle is only used for building the SDK. It is of note however, that this project does not upload the archive to jCenter, this would be a recommended future improvement.

Also note that the SDK itself includes a third party component called [Signpost](https://github.com/mttkay/signpost) for OAuth 1.0 functionality. The project has been stripped down to only include the pieces we need and has been repackaged to avoid prevent conflicts with consuming code. These source files retain their original source code licenses and are Apache licensed.

### Testing

There are a comprehensive set of unit tests that cover all SDK functionality via mocking out the API (web layer). Additionally, there is a small set of happy path integration tests that hit the live API with a set of test credentials. These tests can be run through as mentioned above.

Since the tests currently require and leverage some Android components, and as such need to be run as Android tests instead of plain JUnit tests. This means that an Android device (physical device or emulator) must be connected in order to run the tests. A future consideration may be to remove the Android dependencies in order to run these without a connected Android device.

The tests can be run also be run directly through Android Studio, though at the time of writing this, Android Studio will not automatically add the test configuration. To do so manually:
 1. Go to Run -> Edit Configurations
 2. Click the + at the top left, and select Android Tests.
 3. On the right panel, change the module to richrelevance.
 4. Hit OK

With the configuration set up, you can select it at the top and hit the run button to run the tests.

# Usage Example

The following is a very brief example that demonstrates basic SDK usage:

In Application.onCreate():

```        
        // First create a configuration and use it to configure the default client.
        ClientConfiguration config = new ClientConfiguration("showcaseparent", "bccfa17d092268c0");
        config.setApiClientSecret("r5j50mlag06593401nd4kt734i");
        config.setUserId("RZTestUserTest");
        config.setSessionId(UUID.randomUUID().toString());

        RichRelevance.init(this, config);

        // Enable all logging
        RichRelevance.setLoggingLevel(RRLog.VERBOSE);
```

In an Activity or view logic...

```        
        // Create a "RecommendationsForPlacements" builder for the "add to cart" placement type.
        Placement placement = new Placement(Placement.PlacementType.ADD_TO_CART, "prod1");
        RichRelevance.buildRecommendationsForPlacements(placement)
                // Attach a callback
                .setCallback(new Callback<PlacementResponseInfo>() {
                    @Override
                    public void onResult(PlacementResponseInfo result) {
                        PlacementResponse placement = result.getPlacements().get(0);
                        RecommendedProduct product = placement.getRecommendedProducts().get(0);

                        product.trackClick();
                    }

                    @Override
                    public void onError(Error error) {
                        Log.e(getClass().getSimpleName(), "Error: " + error.getMessage());
                    }
                })
                // Execute the request
                .execute();
```

# Architecture

Below is a high-level architecture for the SDK, detailed in some cases at the class-level. 

## SDK Central

There is a central SDK class (```RichRelevance```) that is the entrypoint for all SDK interactions. It includes the following functionality:

 * Configure logging
 * Access default API client
 * Request builder factory methods

This class must be initialized before the SDK is used by calling ```RichRelevance.init(Context, ClientConfiguration)```. This is typically done in the containing application's ```Application.onCreate()```.

## Constructing API Requests (Builders)

All API requests are constructed using an implementation of the builder pattern. There are several request builders that provide API endpoint abstractions. The builders are as follow:

 * ```RequestBuilder```: base class for all builders. Includes common functionality as well as the ability to add any arbitrary key/value pairs to a request.  
 * ```PlacementsBuilder```: intermediate class for PlacementRecommendationsBuilder and PlacementPersonalizeBuilder. Includes common functionality.
 * ```PlacementPersonalizeBuilder```: "personalize" builder
 * ```PlacementRecommendationsBuilder```: "recsForPlacements" builder
 * ```ProductBuilder```: "getProducts" builder
 * ```StrategyRecommendationsBuilder```: "recsUsingStrategy" builder
 * ```UserPreferenceBuilder```: "user/preferences" read/write builder
 * ```UserProfileBuilder```: "UserProfile" builder
 
Each builder has type-safe methods for setting relevant values, the ability to add a completion listener, and an ```execute()``` method which performs the request via the set client (by default this is set to the SDK's default client). There are also helper methods for common use-cases that create pre-configured builders (see next section). Internally, the builders are also responsible for parsing the result of the web request into domain specific objects.

### Builder “Helper” Methods

The ```RichRelevance``` class contains several "factory" methods that vend pre-configured request builders for common API use-cases. These methods are separated into two groups, one for fetches and one for tracking. The former includes requests that expect responses such as recommended products, while the latter includes tracking requests that are essentially fire and forget.

## Networking

The network operations performed by this SDK are at this time, not diverse enough to warrant inclusion of any third-party libraries such as OkHttp or Volley. All calls at this time are HTTP GET, all payloads are JSON, and there are only a handful of endpoints and paths. In the future, more granular, REST-style API changes might warrant the introduction of such a library. With that said, a heavily modified and stripped down version of our Android WebServiceManager library is included in order to manage some of the lower level pieces of placing web requests.

### Request Management

The SDK uses an instance of a ```WebRequestManager``` to perform all network operations. The manager will maintain a maximum number of simultaneous connections and handles the background threading, queueing, execution, and callbacks for each request it is passed. In this way, the SDK's network traffic is a bit throttled in an attempt to avoid consuming too much network throughput from the containing app. Under the hood, the manager uses ```HTTPUrlConnection``` to perform requests.

It is also worth noting that click tracking uses a second ```WebRequestManager``` instance to keep view requests from "clogging" the rest of the SDK's functionality. This click tracking manager will only use one more connection.

### API Client

The ```RichRelevanceClient``` is the dispatch point of the SDK. It handles obtaining the request information from a ```RequestBuilder```, executing the request through the ```WebRequestManager```, and passing the result back to any callback associated with the ```RequestBuilder```. The client also contains a ```ClientConfiguration``` which contains a set of user credentials for the API. This is set up in the SDK initialization, but can be modified in the clients at a later time if needed.

It is possible to create a standalone instance of this class, but more advisable to use the one supplied by ```getDefaultClient()``` found on the ```RichRelevance``` class. 

### Requests

API requests originate from request builders (see above) as ```WebRequest``` objects which are executed via a ```WebRequestManager```. This ```WebRequest``` object contains all the elements of the request inside a ```WebRequestBuilder``` object (URL, HTTP method, parameters, headers, OAuth settings, etc) as well a way to parse the response of the request back into domain specific model objects. The ```RequestBuilder``` base class internally maintains the parameters of the web call via methods which delegate to the underlying ```WebRequest``` object.

### Response

```RequestBuilder``` is also responsible for the parsing of the response since the response format is just as domain specific as the parameters. The base class does some error handling and extracts the JSON response if possible. It then delegates the JSON deserialization to the implementing subclasses.

### Connectivity

In low or no network scenarios, for the majority of calls, the SDK does not queue or retry failed requests. If no network is present and concretely detectable, requests will not be attempted at all and appropriate errors will be reported to calling code. 

### Click Tracking

Product recommendation requests may return a set of ```RecommendedProduct```. Clicks on these can be tracked either by calling ```trackClick()``` directly on the object or via ```RichRelevance.trackClick(RecommendedProduct)```. 

Click tracking is an exception to the rules on connectivity: if a request fails, it will be retained in an in-memory-queue. The SDK will attempt to listen for network state changes to flush it's queue when the network becomes available. This will only function if the containing application holds the ```ACCESS_NETWORK_STATE``` permission, but will not crash if the permission isn't held, therefore the permission is advantageous but optional. In either case, the next time a click track is sent by the containing application, the ```ClickTrackingManager``` will first attempt to flush its queue. The containing application may also call ```RichRelevance.flushClickTracking()``` to manually trigger a queue flush attempt.

## Logging

The SDK logs to the system logs via ```android.util.Log``` and is configurable with the standard log levels (VERBOSE, DEBUG, INFO, WARN, ERROR, NONE). By default, logging is set to NONE, so as to not incur logging overhead for production releases. The ```RRLog``` class is the location in which all logging logic lives. Levels can be configured via ```RichRelevance.setLoggingLevel(RRLog.LogLevel int)```.

## Error conditions and handling

### Runtime Errors

All SDK calls that make remote requests provide error feedback as part of completion handlers. Furthermore, a best effort has been made to ensure that any SDK thrown exceptions prevent the hosting app from crashing while still maintaining integrity of the SDK. See ```Error.ErrorType``` for a list of possible error codes. 

### API Response Validation

Despite the fact that the API is stable and well-documented, assumptions are not made as per the presence of fields in API responses. Expected fields are validated and calls fail gracefully in the face of unexpected API behavior. 

### Input Validation

All assumptions regarding SDK inputs (arguments passed to SDK methods by the parent app) are validated directly and handled accordingly. If the method consumes an error handler, the handler is invoked with an appropriate error code, otherwise the operation is aborted and error is logged. 

## Security

All SDK requests are performed over HTTPS by default and include API authentication parameters to ensure the maximum level of security. In situations where the API supports it, OAuth 1.0 is used to sign requests. The SDK does not store any data to disk, therefore on-disk encryption is not a concern at this time. 

## Release to Bintray

The release process to bintray is automated with the command ```./gradle bintrayUpload```.

Public properties for bintray such groupId, artifact name, version... are specified in the gradle.properties file.

**NOTE:** verify all the values on gradle.properties before release

To upload files to bintray it is necessary to add user/key properties to the local.properties file like:
```
bintray_user=
bintray_key=
```


