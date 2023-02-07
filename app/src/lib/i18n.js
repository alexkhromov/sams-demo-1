import i18n from 'i18next';
import XHR from 'i18next-xhr-backend';
import { initReactI18next } from 'react-i18next';
import { BASE_URL } from './Constraints';

i18n
    .use(XHR)
    .use(initReactI18next)
    .init({
        debug: true,
        lng: 'en',
        fallbackLng: 'en',
        load: 'languageOnly',

        backend: {
            loadPath: `${BASE_URL}/resources/i18n/{{lng}}/{{ns}}.json`
        }
    });

export default i18n;