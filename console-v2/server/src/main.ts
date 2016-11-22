"use strict";

import * as bodyParser from "body-parser";
import * as express from "express";
import * as path from "path";

import * as routes from "./routes";

class Server {

    public app: express.Application;

    public static bootstrap(): Server {
        return new Server();
    }

    constructor() {
        this.app = express();
        this.routes();
        this.app.listen(3000, () => {
            console.log("Server listening...");
        });
    }

    private routes() {
        let router = express.Router();
        this.app.use(express.static("ui/dist"));

        this.app.use(bodyParser.json());
        this.app.use(bodyParser.urlencoded());

        let oauthLogin: routes.OAuthLogin = new routes.OAuthLogin();
        router.post("/oauth/authenticate", oauthLogin.oauthLogin);

        let index: routes.Index = new routes.Index();
        router.get("*", (req, res, next) => { res.sendFile("index.html", { root: "ui/dist" }); });

        this.app.use(router);
    }
}

Server.bootstrap();

