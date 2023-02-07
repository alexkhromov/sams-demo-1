import { STORAGE_JWT } from "./Constraints";

class JWT {
    static tokenExpired() {
        return !JWT.payload || (JWT.payload && JWT.payload.exp <= Math.floor(Date.now() / 1000));
    }

    static updateToken(token) {
        let [headers, payload, sign] = token
            .substr(7)
            .split('.')
            .map((value, index) => index < 2 ? JSON.parse(atob(value)) : value);
        JWT.payload = payload;
        JWT.payload.metadata = JSON.parse(atob(payload.metadata));
    }

    static clearStorage() {
        JWT.payload = undefined;
        sessionStorage.removeItem(STORAGE_JWT);
    }

    static setStorage(token) {
        sessionStorage.setItem(STORAGE_JWT, token);
        JWT.updateToken(token);
    }

    static getStorage() {
        return sessionStorage.getItem(STORAGE_JWT) || "";
    }
}

export default JWT;