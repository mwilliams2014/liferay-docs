# Optimizing Themes

Are you applying the same modifications to each of your themes? Do you want to 
make a temporary change to your theme's UI? [Themelets](/develop/tutorials/-/knowledge_base/7-0/themelets) 
are the answer! Themelets are small, extendable, and, reusable, modular pieces 
of code, that allow you to quickly make changes to your theme. Because they are 
modular, you can use the same themelet for multiple themes! Do you want to add 
the same UI modification to all of your themes, without rewriting the code? Use 
a themelet! Do you want to share your new theme designs with a colleague? Use a 
themelet! Do you want to test out a new design concept without altering your 
theme's code? Use a themelet!

Themelets are just one of the many benefits gained when you migrate your theme
from an Ant based project to a [Themes Generator](/develop/tutorials/-/knowledge_base/7-0/themes-generator) 
based project.

The Themes Generator is a Node.js-based tool that allows you to develop and 
manage your theme using [theme gulp tasks](/develop/reference/-/knowledge_base/7-0/theme-gulp-tasks).
These tasks allow you to run typical processes such as building and deploying, 
but they offer more as well. For instance, you can automatically deploy your 
theme when changes are made, or set the app server for your theme. Do you need 
to make changes to your theme's settings? No problem. You can quickly and easily 
configure your theme's settings through the handy command-line-wizard that the
Themes Generator provides. Just answer a few questions about the settings, and
in no time at all your theme's files are automatically updated.

As you can see, The Node.js development tools offer a lot to a Liferay theme 
developer. In this section of tutorials, you will learn how to migrate your Ant 
based theme project to a Themes Generator based project, so you can start using
these great features!
