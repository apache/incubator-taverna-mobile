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
# Apache Taverna Mobile

Apache Taverna Mobile is planned as an Android app
for controlling an [Apache Taverna Server](http://taverna.incubator.apache.org/documentation/server/) 
for remotely running 
[Apache Taverna](http://taverna.incubator.apache.org/) workflows.

This module is **work in progress** as part of Google Summer of Code 2015.



## License

(c) 2015 Apache Software Foundation

This product includes software developed at The [Apache Software
Foundation](http://www.apache.org/).

Licensed under the [Apache License
2.0](https://www.apache.org/licenses/LICENSE-2.0), see the file
[LICENSE](LICENSE) for details.

The file [NOTICE](NOTICE) contain any additional attributions and
details about embedded third-party libraries and source code.


# Contribute

Please subscribe to and contact the 
[dev@taverna](http://taverna.incubator.apache.org/community/lists#dev mailing list)
for any questions, suggestions and discussions about 
Apache Taverna Mobile.

Bugs and feature plannings are tracked in the Jira
[Issue tracker](https://issues.apache.org/jira/browse/TAVERNA/component/12326901)
under the `TAVERNA` component _GSOC Taverna Mobile. Feel free 
to add an issue!

To suggest changes to this source code, feel free to raise a 
[GitHub pull request](https://github.com/apache/incubator-taverna-mobile/pulls).
Any contributions received are assumed to be covered by the [Apache License
2.0](https://www.apache.org/licenses/LICENSE-2.0). We might ask you 
to sign a [Contributor License Agreement](https://www.apache.org/licenses/#clas)
before accepting a larger contribution.


# Building and install requirements

* Android Studio at least version 1.1.0
* Android Build tools version 21.1.2+
* gradle version 1.1.0+
* Support libraries for CardViews and recycler views. These are already configured in the gradle files
Import the project as Android Studio Project into Android Studio after downloading from the github repository.
Create and startup a virtual device or connecct your mobile device if ready.
Once the devices are ready, build and run the project. Select the target devvice on which to install and launch the app.
Once installed, you get a flash screen containing the logo and Name of the app and some powered by Text.


# Usage
Launch and Login
Launch the application to get started. For first time use when the application is started, you will be prompted with a login screen. This login accepts MyExperiment accounts. You will need to first create one such account in order to login. You can decide whether to remain logged-in or be logged-out when your session expires. You can also configure the login persistence from the settings option in the menu after you login.
A successful login would lead you to a dashboard or home screen of the application.
Users need to go to the settings page and configure their Taverna Player accounts and mount points to indicate which server should be used to run their workflows. This setting could change per organisation.

Dashboard or Home
The dashboard has two swipeable tabs. These tabs represent screens that hosts workflow streams.
once logged-in a stream of workflows should appear on the first screen(Wrofklows). The second tab holds workflows that have been favorited or saved for offline reference.
On the first tab, you could pull down to referesh initial workflows. To load more workflows, just scroll to the end of the current stream. more workflows would be loaded and added to the current ones as the user reaches the end of the current stream.
The search bar icon allows you to search and find workfows by author or by name. the results are provided in real-time in the first tab (First page).
To mark a workflow as favorite, you can just tap the favorite button for the given workflow.

Workflow Detail
To view details and actually run a workflow, click the view button for the given workflow. Details are fetched from myxperiment and presented on the current page. The details page has three main swipeable tabs. The second tab shows a list  of the runs performed on this workflow and the last tab is for some policy information. On the first page of the details, users can initiate a workflow run, download a workflow or mark them as favorite with the appropriate button.

Application Menus
The navigation drawer can be pulled out from the home page by swiping the extreme left of screen across the screen. This menu contains the following items:
 - Workflows: enters the workflow screen from any other screen.
 - Open workflow: prompts users to pick a workflow to run from an external storage location or Dropbox.
 - Usage: presents usrs with usage dialog with usage information.
 - About: presents an about dialog for the application.
 - Settings: Provides a settings page for users to configure preference paramters an options like player portal location and credentials.
 - Logout: Logs a user out of the application and closes the app.


* ...

# Documentation

_TODO_

* ...

