"use strict";

import * as express from "express";
import * as path from "path";

namespace Route {

    export class OAuthLogin {
        public oauthLogin(req: express.Request, res: express.Response, next: express.NextFunction) {
            console.log("Token received!");
            res.end();
        }
    }

    export class Index {
        public index(req: express.Request, res: express.Response, next: express.NextFunction) {
            res.sendFile("index.html", { root: "dist/ui" });
        }
    }
}

export = Route;