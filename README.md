Configurator
============
Configurator is a lightweight Java configuration package that eases the pain of configuring (web) apps under different
contexts.  Offers easy to use Spring and servlet integration.

Features:
=========
- Easy to setup different contexts via JVM params
- Configuration is completely file-based
- Dynamic reloading of values without needing to restart the app
- Supports many data types: String, Int, Long, Boolean, List
- Reduces configuration mistakes by ensuring clean separation by environments/hosts
- Developers can check-in different versions of the application config files based on environment/host (ex: staging.awesomeApp.properties) without worrying about affecting production configs
- Overriding properties is as easy as adding new values for same properties in different contexts (env/host)
- Reusing base property values is as easy as commenting out environment/host specific ones
    - comment out host-based prop ==> pickup env-based
    - comment out env-based ==> pickup base
- Supports Spring's PropertyPlaceholderConfigurer for dynamically configuring spring beans via placeholders
- Supports integration with ServletContextListener to make configs available inside JSPs via JSTL/EL notation like ${map[key]}

Use Cases:
==========
1) dev: override local/dev configs without changing production configs
- option 1: use local context and define a config path outside of project ==> no risk of checking in bad configs
- option 2: shadow base config files with host or env-specific ones inside the project ==> easier to access via IDE

2) staging: override production configs in a persistent way that survives frequent builds
- use local context (config path outside of project) ==> will not interfere with builds and does not require modifying war/jar files

3) production: change config on some instances of the app
- option 1: use local context (config path outside of project) ==> will not interfere with deployed war/jar
- option 2: use host context configuration (packaged with war/jar) ==> will only impact specific hosts

Examples:
=========

// load properties file app.properties and refresh every 10 seconds
FileConfig fileConfig = new FileConfig("app.properties", 10);

// read string property 'app.value' and use 'UNKNOWN' as default
String value = fileConfig.getString("app.value", "UNKNOWN");


License: MIT

