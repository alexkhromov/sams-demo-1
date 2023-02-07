import React from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { Trans } from 'react-i18next';
import LocaleContext from '../../contexts/LocaleContext';
import { usePageNumberCallback, usePageSizeCallback } from '../../hooks/PageInfoHooks';
import { API } from '../../lib/API';
import { PAGE_SIZES, STATUS, USERS_ENDPOINT } from '../../lib/Constraints';
import { fireGlobalErrors } from '../../lib/Errors';
import Dropdown from '../Dropdown/Dropdown';
import PaginationNav from '../PaginationNav/PaginationNav';
import UserTable from './UserTable';

function UsersTab({ pageNumber, pageSize }) {
    const [pageData, setPageData] = React.useState({});
    const [forceUpdate, setForceUpdate] = React.useState(false);
    const locale = React.useContext(LocaleContext);

    const setPageNumberCallback = usePageNumberCallback();
    const setPageSizeCallback = usePageSizeCallback();
    
    React.useEffect(() => {
        let params = {
            pageSize: pageSize,
            pageNum: pageNumber
        };

        let headers = new Headers();
        headers.set('Accept-Language', locale.full);

        API.get({ endpoint: USERS_ENDPOINT, params: params, headers: headers })
            .then((data) => {
                data.json().then((pageData) => {
                    if (pageNumber * pageSize > pageData.total) {
                        setPageNumberCallback(Math.floor((pageData.total - 1) / pageSize));
                    }
                    setPageData(pageData);
                });
            });
    }, [pageNumber, pageSize, setPageNumberCallback, forceUpdate, locale.full]);

    const totalElements = pageData.total || 0;
    const totalPages = Math.floor((totalElements + pageSize - 1) / pageSize);
    const firstOnPage = pageNumber * pageSize + 1;
    const lastOnPage = Math.min(totalElements, firstOnPage + pageSize - 1);

    const paginationNav = React.useMemo(() => {
        return (
            <PaginationNav
                currentPage={pageNumber}
                totalPages={totalPages}
                setCurrentPageCallback={setPageNumberCallback}
            />
        );
    }, [pageNumber, totalPages, setPageNumberCallback]);

    const nav = (
        <nav className="d-flex justify-content-between">
            <span className="font-weight-bold text-info border-top border-info">
                <Trans i18nKey="entries.showing" ns="table">
                    Showing {{firstOnPage}} to {{lastOnPage}} of {{totalElements}} entries
                </Trans>
            </span>

            {paginationNav}
        </nav>
    );

    const deleteCallback = React.useCallback((userId) => {
        let headers = new Headers();
        headers.set('Accept-Language', locale.full);

        API.delete({ endpoint: USERS_ENDPOINT, url: `${userId}`, headers: headers })
            .then((response) => {
                if (response.ok) {
                    setForceUpdate(!forceUpdate);
                } else {
                    response.json().then((result) => {
                        if (result.status === STATUS.FAILURE) {
                            fireGlobalErrors(result.errorData);
                        }
                    });    
                }
            });
    }, [forceUpdate, locale.full]);

    return (
        <main className="d-flex flex-row justify-content-start pt-3">
            <div className="container-fluid mx-5">

                <div className="row mb-3">
                    
                    <div className="col-2" />
                    
                    <div className="col-10 d-flex flex-row justify-content-between">
                        <div>
                            <span className="pb-2 font-weight-bold text-info border-bottom border-info">
                                <Trans i18nKey="entries.limit" ns="table">
                                    Show
                                    
                                    <Dropdown title={`${pageSize}`}>
                                        {PAGE_SIZES.map((value) => {
                                            return (
                                                <button 
                                                    className="dropdown-item" 
                                                    onClick={() => {setPageSizeCallback(value)}}
                                                    key={value.toString()}
                                                >
                                                    {value}
                                                </button>
                                            );  
                                        })}
                                    </Dropdown>
                                    
                                    entries
                                </Trans>
                            </span>                            
                        </div>

                    </div>  

                </div>

                <div className="row">
                    <div className="col-2">
                        
                    </div>

                    <div className="col-10 d-flex flex-column">
                        <UserTable 
                            users={pageData.data || []} 
                            deleteCallback={deleteCallback}
                        /> 
                        {nav}
                    </div>
                </div>

            </div>
        </main>
    );
}

export default UsersTab;