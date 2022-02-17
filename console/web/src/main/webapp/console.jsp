<!--
	Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others All rights reserved.

    This program and the accompanying materials are made
    available under the terms of the Eclipse Public License 2.0
    which is available at https://www.eclipse.org/legal/epl-2.0/

    SPDX-License-Identifier: EPL-2.0

    Contributors:
        Eurotech - initial API and implementation
-->

<!--
    ** READ ME **
    *************

	This is the original console.jsp index file.

    While developing is it possible to use this file,
    but any modification MUST be manually copied into console.jsp
    after minification of HTML.

    It has been minified in to improve page loading.
-->
<!doctype html>
<html lang="en">
    <head>
        <!--
            Meta tag informations
         -->
        <meta http-equiv="content-type" content="text/html; charset=UTF-8">
        <meta http-equiv="X-UA-Compatible" content="IE=9">

        <!-- Manage the localization of the console -->
        <% if (request.getParameter("l") != null) {  %>
        	<meta name="gwt:property" content="locale=<%=request.getParameter("l")%>">
        <% } else if (request.getHeader("Accept-Language") != null) { %>
	        <meta name="gwt:property" content="locale=<%=request.getHeader("Accept-Language").split(",")[0]%>">
        <% } else { %>
	        <meta name="gwt:property" content="locale=en">
	    <% } %>

        <!--
            Favicon and title definition
         -->
        <title>Eclipse Kapua&trade; Console</title>
        <link rel="icon" type="image/ico" href="img/icon-color.ico"
    sizes="32x32" />
        <!--
            CSS resources
         -->
        <link rel="stylesheet" type="text/css" href="gxt-2.2.5/css/gxt-all.css" id="gxtCss">
        <link rel="stylesheet" type="text/css" id="gray" href="gxt-2.2.5/css/gxt-gray.css">

        <link rel="stylesheet" href="fontAwesome/css/font-awesome.min.css">

        <!--
            JS resources
         -->
        <script type="text/javascript" src="admin.nocache.js"></script>

        <!-- This script manage all JS/CSS deferred loading -->
        <script type="text/javascript" src="js/kapuaconsole/console.js" defer></script>

    </head>
    <body>
    </body>
</html>
