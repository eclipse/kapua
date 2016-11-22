"use strict";

import * as express from "express";
import * as path from "path";
import * as request from "request";
import * as bodyParser from "body-parser";
import * as http from "http";

let kapuaServerConf = require("../src/conf/kapua-server.config.json");

namespace Route {

    export class OAuthLogin {
        public oauthLogin(req: express.Request & bodyParser.Parsed, res: express.Response, next: express.NextFunction) {
            request.post(kapuaServerConf.oauth.tokenEndpoint, {
                form: {
                    "grant_type": "authorization_code",
                    "client_id": kapuaServerConf.oauth.clientId,
                    "code": req.body.code,
                    "redirect_uri": req.body.redirectUri
                },
                json: true
            }, (error: any, tokenResponse: http.IncomingMessage & bodyParser.Parsed, body: any) => {
                request.post(kapuaServerConf.restApi.baseUrl + "/api/v1/authentication/jwt", {
                    body: {
                        jwt: tokenResponse.body.id_token
                    },
                    json: true
                }, (error: any, restApiResponse: http.IncomingMessage & bodyParser.Parsed, body: any) => {
                    res.json(restApiResponse.body);
                });
            });
        }
    }

    export class Index {
        public index(req: express.Request, res: express.Response, next: express.NextFunction) {
            res.sendFile("index.html", { root: "ui/dist" });
        }
    }
}

export = Route;