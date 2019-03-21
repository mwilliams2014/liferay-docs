# Migrating a Vue App to @product@

The example Vue Guestbook uses single file components (`.vue` files) with 
multiple views that use the `vue-router` to navigate between them. The steps 
below demonstrate how easy it is to migrate this type of app to @product@.

Follow these steps:

1.  Install the Liferay Bundle Generator and its dependencies with the command 
    below, if they're not already installed:
    
        npm install -g yo generator-liferay-bundle

2.  Generate a bundle with the 
    [Yeoman]() 
    generator, select Vue, and follow the prompts:

        yo liferay-bundle

3.  Replace the `Devdependencies` in the `package.json` with these:

        "liferay-npm-bundler": "^2.6.2",
        "liferay-npm-build-support": "^2.6.2",
        "copy-webpack-plugin": "^4.5.4",
        "webpack": "^4.0.0",
        "webpack-cli": "^3.0.0",
        "webpack-dev-server": "^3.0.0",
        "@babel/cli": "^7.0.0",
        "babel-preset-env": "^1.7.0",
        "babel-loader": "^7.0.0",
        "@vue/cli-plugin-babel": "^3.4.0",
        "@vue/cli-plugin-eslint": "^3.4.0",
        "@vue/cli-service": "^3.4.0",
        "babel-eslint": "^10.0.1",
        "eslint": "^5.13.0",
        "eslint-plugin-vue": "^5.1.0",
        "vue-template-compiler": "^2.6.1",
        "vueify": "9.4.1"
        
4.  Copy over any dependencies to the generated  `package.json` such as 
    `vue-router`.
    
5.  Copy the `eslintConfig` entry over to the `package.json`. The configuration 
    for the Vue Guestbook is shown below:
<!--
Not sure if this is required
-->
        "eslintConfig": {
          "root": true,
          "env": {
            "node": true
          },
          "extends": [
            "plugin:vue/essential",
            "eslint:recommended"
          ],
          "rules": {},
          "parserOptions": {
            "parser": "babel-eslint"
          }
        },
        
6.  Replace the build script in the `package.json` with the one below to use 
    `vue-cli-service`. The updated build script uses vue-cli to access the main 
    entrypoint for the app (`index.js` in the example below) and combines all 
    the Vue templates and JS files into one single file named `index.common.js`:
    
        babel --source-maps -d build src && vue-cli-service build --dest build/ 
        --formats commonjs --target lib --name index ./src/index.js && npm run 
        copy-assets && liferay-npm-bundler"

6.  Update the `main` entry of the `package.json` to match the new commonjs 
    file:
    
        "main": "index.common"

6.  Update the generated `.babelrc` file to use `@babel/preset-env` instead of 
    `env`:

        "presets": ["@babel/preset-env"]
        
7.  Update any static resource relative links to use the `web-context` path 
    defined in the generated bundle's `.npmbundlerrc` file. For example, the Vue 
    Guestbook's updated logo image `src` has the configuration below:
    
        <img alt="Vue logo" src="/o/vue-guestbook-migrated/logo.png">
        
8.  Copy any folders, such as the `components` folder, Vue templates, and JS 
    files over to the generated `src` folder.
    
9.  Copy any additional assets, such as images, to the `assets` folder and copy 
    your CSS files over to the `assets/css` folder. 
    
10.  Add your app's main file code (such as `index.js`) over to the generated 
     `index.js` file, within the `main()` function and pass the 
     `#${portletElementId}` as the element to render your Vue app inside. Vue 
     Guestbook's configuration is shown below:
     
        new Vue({
         el: `#${portletElementId}`,
         render: h => h(App),
         router
        })
        
11.  Finally, deploy your portlet with the command below:

        npm run deploy