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
        let router: express.Router;
        router = express.Router();

        let oauthLogin: routes.OAuthLogin = new routes.OAuthLogin();
        router.get("/oauth/login", oauthLogin.oauthLogin);

        let index: routes.Index = new routes.Index();
        router.get("/", index.index);

        this.app.use(router);
        this.app.use(express.static("dist/ui"));
    }
}

Server.bootstrap();

