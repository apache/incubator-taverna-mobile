<!--
   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
-->
# Status 
 [![Join the chat at https://gitter.im/apache/taverna](https://badges.gitter.im/apache/taverna.svg)](https://gitter.im/apache/taverna?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)
 [![Build Status](https://travis-ci.org/apache/incubator-taverna-mobile.svg?branch=master)](https://travis-ci.org/apache/incubator-taverna-mobile)
# Apache Taverna Mobile

Apache Taverna Mobile is an Android app
for controlling an [Apache Taverna Server](http://taverna.incubator.apache.org/documentation/server/)
for remotely running
[Apache Taverna](http://taverna.incubator.apache.org/) workflows.

It can also talk to a [Taverna Player](https://github.com/myGrid/taverna-player-portal).

This module was created during Google Summer of Code 2015.



## License

(c) 2015-2018 Apache Software Foundation

This product includes software developed at The [Apache Software
Foundation](http://www.apache.org/).

Licensed under the [Apache License
2.0](https://www.apache.org/licenses/LICENSE-2.0), see the file
[LICENSE](LICENSE) for details.

The file [NOTICE](NOTICE) contain any additional attributions and
details about embedded third-party libraries and source code.


# Contribute

Please subscribe to and contact the
[dev@taverna](http://taverna.incubator.apache.org/community/lists#dev) mailing list
for any questions, suggestions and discussions about
Apache Taverna Mobile.

Bugs and feature plannings are tracked in the Jira
[Issue tracker](https://issues.apache.org/jira/browse/TAVERNA/component/12326901)
under the `TAVERNA` component _GSOC Taverna Mobile._ Feel free
to add an issue!

To suggest changes to this source code, feel free to raise a
[GitHub pull request](https://github.com/apache/incubator-taverna-mobile/pulls).
Any contributions received are assumed to be covered by the [Apache License
2.0](https://www.apache.org/licenses/LICENSE-2.0). We might ask you
to sign a [Contributor License Agreement](https://www.apache.org/licenses/#clas)
before accepting a larger contribution.


# Building and install requirements

* Android Studio at least version 3.0.0+
* Android Build tools version 27.0.3
* Android SDK for API 27
* gradle version 4.4
* Support libraries for CardViews and recycler views. These are already configured in the gradle files
Import the project as Android Studio Project into Android Studio after downloading from the github repository.
Create and startup a virtual device or connecct your mobile device if ready.
Once the devices are ready, build and run the project. Select the target devvice on which to install and launch the app.
Once installed, you get a flash screen containing the logo and Name of the app and some powered by Text.
* Also import AndroidStyle.xml to your android studio's codestyle. ( You can found this file at the project's root [file link](https://github.com/apache/incubator-taverna-mobile/blob/master/))


# Usage | Quick start

## Launch and Login  

Launch the application to get started. For first time use when the application is started, you will be prompted with a login screen. This login accepts MyExperiment accounts. You will need to first create one such account in order to login. You can decide whether to remain logged-in or be logged-out when your session expires. You can also configure the login persistence from the settings option in the menu after you login.
A successful login would lead you to a dashboard or home screen of the application.
Users need to go to the settings page and configure their Taverna Player accounts and mount points to indicate which server should be used to run their workflows. This setting could change per organisation.

## Dashboard or Home  

The dashboard has two swipeable tabs. These tabs represent screens that hosts workflow streams.
once logged-in a stream of workflows should appear on the first screen(Wrofklows). The second tab holds workflows that have been favorited or saved for offline reference.  
On the first tab, you could pull down to referesh initial workflows. To load more workflows, just scroll to the end of the current stream. more workflows would be loaded and added to the current ones as the user reaches the end of the current stream.  
The search bar icon allows you to search and find workfows by author or by name. the results are provided in real-time in the first tab (First page).  
To mark a workflow as favorite, you can just tap the favorite button for the given workflow.

## Workflow Detail  

To view details and actually run a workflow, click the view button for the given workflow. Details are fetched from myxperiment and presented on the current page. The details page has three main swipeable tabs. The second tab shows a list  of the runs performed on this workflow and the last tab is for some policy information. On the first page of the details, users can initiate a workflow run, download a workflow or mark them as favorite with the appropriate button.

## Application Menus  

The navigation drawer can be pulled out from the home page by swiping the extreme left of screen across the screen. This menu contains the following items:
 - Workflows: enters the workflow screen from any other screen.
 - Open workflow: prompts users to pick a workflow to run from an external storage location or Dropbox.
 - Usage: presents usrs with usage dialog with usage information.
 - About: presents an about dialog for the application.
 - Settings: Provides a settings page for users to configure preference paramters an options like player portal location and credentials.
 - Logout: Logs a user out of the application and closes the app.


# Documentation

Taverna Mobile has further [documentation](https://docs.google.com/document/d/1G3AmW-zgsOxNg81uOWOUVISfaimp9Ku5k1ntIFm8hvo/edit?usp=sharing)
about key functionalities and implementations with screenshots attached.

It has been mainly adapted for developers, however, users can get neccessary information from the quick start section of this readme.


# Using your own Taverna Server

The defaults for this applications uses a development instance of Taverna Server at University of Manchester, which might not be available.

You can start our own [Taverna Server](https://hub.docker.com/r/taverna/taverna-server/) with 
[Docker](https://www.docker.com/):

    docker run -p 8090:8080 --name taverna-server -d taverna/taverna-server:2.5.4

And a [Taverna Player](https://hub.docker.com/r/fbacall/taverna-player-portal/):

    docker run --name taverna-portal --link taverna-server:taverna -p 3000:3000 -d fbacall/taverna-player-portal

Then edit [app/src/main/res/values/strings\_activity\_settings.xml](app/src/main/res/values/strings_activity_settings.xml) to set:


```xml
    <string name="pref_player_default"> http://example.com:3000/</string>   <!-- default value -->
    <string name="pref_server_default"> http://example.com:8090/</string>   <!-- default value -->
```

.. where you= replace `example.com` with the hostname or IP address of your server running Docker. 

Note that if you are using Docker on OS X or Windows, then a Virtual Machine will run the Docker
containers under a different IP address. Use `docker-machine ip` to check. You may have to
adjust your firewalls to allow port `3000` and `8090` from the Taverna Mobile app. If you are 
testing from a mobile/tablet, you may have to use WiFi to get access to the
Taverna Server on the local network.

You can alternatively install the [Taverna
Server](http://taverna.incubator.apache.org/download/server/) WAR file in your
favourite servlet container, e.g. [Apache Tomcat](http://tomcat.apache.org/) - see
the [Taverna Server installation guide](https://launchpad.net/taverna-server/2.5.x/2.5.4/+download/install.pdf)
for details.

# Export restrictions

This distribution includes cryptographic software.
The country in which you currently reside may have restrictions
on the import, possession, use, and/or re-export to another country,
of encryption software. BEFORE using any encryption software,
please check your country's laws, regulations and policies
concerning the import, possession, or use, and re-export of
encryption software, to see if this is permitted.
See <http://www.wassenaar.org/> for more information.

The U.S. Government Department of Commerce, Bureau of Industry and Security (BIS),
has classified this software as Export Commodity Control Number (ECCN) 5D002.C.1,
which includes information security software using or performing
cryptographic functions with asymmetric algorithms.
The form and manner of this Apache Software Foundation distribution makes
it eligible for export under the License Exception
ENC Technology Software Unrestricted (TSU) exception
(see the BIS Export Administration Regulations, Section 740.13)
for both object code and source code.

The following provides more details on the included cryptographic software:

* Uses [Apache HttpComponents](https://hc.apache.org/) client, which can initiater `https://` connections 
  through [Android SDK](https://code.google.com/p/android/issues/detail?id=186608)'s SSL support
* Uses [Dropbox Android SDK](https://www.dropbox.com/developers-v1/core/sdks/android), 
  which includes a `SecureSSLSocketFactory` binding with Android SDK
