

## Development

Before you can build this project, you must install and configure the following dependencies on your machine:

1. [Node.js][]: We use Node to run a development web server and build the project.
   Depending on your system, you can install Node either from source or as a pre-packaged bundle.

After installing Node, you should be able to run the following command to install development tools (like
[Bower][] and [BrowserSync][]). You will only need to run this command when dependencies change in package.json.

    npm install

We use [Gulp][] as our build system. Install the Gulp command-line tool globally with:

    npm install -g gulp

Run the following commands in two separate terminals to create a blissful development experience where your browser
auto-refreshes when files change on your hard drive.

    ./mvnw
    gulp

Bower is used to manage CSS and JavaScript dependencies used in this application. You can upgrade dependencies by
specifying a newer version in `bower.json`. You can also run `bower update` and `bower install` to manage dependencies.
Add the `-h` flag on any command to see how you can use it. For example, `bower update -h`.


## Building for production

To optimize the trakeye client for production, run:

    ./mvnw -Pprod clean package

This will concatenate and minify CSS and JavaScript files. It will also modify `index.html` so it references
these new files.

To ensure everything worked, run:

    java -jar target/*.war

Then navigate to [http://localhost:8080](http://localhost:8080) in your browser.

## Testing

Unit tests are run by [Karma][] and written with [Jasmine][]. They're located in `src/test/javascript/` and can be run with:

    gulp test



## Continuous Integration

To setup this project in Jenkins, use the following configuration:

* Project name: `trakeye`
* Source Code Management
    * Git Repository: `git@github.com:xxxx/trakeye.git`
    * Branches to build: `*/master`
    * Additional Behaviours: `Wipe out repository & force clone`
* Build Triggers
    * Poll SCM / Schedule: `H/5 * * * *`
* BuildBuild
    * Invoke Maven / Tasks: `-Pprod clean package`
* Post-build Actions
    * Publish JUnit test result report / Test Report XMLs: `build/test-results/*.xml`


[Node.js]: https://nodejs.org/
[Bower]: http://bower.io/
[Gulp]: http://gulpjs.com/
[BrowserSync]: http://www.browsersync.io/
[Karma]: http://karma-runner.github.io/
[Jasmine]: http://jasmine.github.io/2.0/introduction.html
[Protractor]: https://angular.github.io/protractor/


## Gps device registration and traking

To setup and track GPS device in Trakeye, user the following steps

* Login as super admin
* Create a gpsadmin
* Login as gspadmin
* Click on entity management
* Click on Trakeye types
* Click on create trakeye type
* Create a trakeye type with name as GPSDeviceType and attributes with attribute 1 as "key" and attribute 2 as "cmd".
* Save trakeye type
* Click on user management
* Register two users for two GPS devices with role as ROLE_USER. While selecting trakeye type select GPSDeviceType and enter "key" and "cmd" value as i suggested earlier.
* Once users are registered now gpsadmin and respective hierarchy can view the two device locations in live monitoring portal