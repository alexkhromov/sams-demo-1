import React from 'react';
import ReactDOM from 'react-dom';
import { library } from '@fortawesome/fontawesome-svg-core';
import { faEdit, faTrashAlt } from '@fortawesome/free-regular-svg-icons';
import { faExclamationTriangle, faExternalLinkAlt, faPlus, faSearch } from '@fortawesome/free-solid-svg-icons';
import App from './components/App/App';
import './index.css';
import './lib/i18n';

library.add(faExternalLinkAlt, faEdit, faTrashAlt, faPlus, faSearch, faExclamationTriangle);

ReactDOM.render(
    (
        <React.Suspense fallback={<span>Loading</span>}>
            <App />
        </React.Suspense>
    ), document.getElementById('root')
);