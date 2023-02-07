import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import React from 'react';
import { Trans } from 'react-i18next';
import { Link } from 'react-router-dom';
import LocaleContext from '../../contexts/LocaleContext';
import UserContext from '../../contexts/UserContext';
import { usePageNumberCallback, usePageSizeCallback } from '../../hooks/PageInfoHooks';
import { API } from '../../lib/API';
import { PAGE_SIZES, STATUS } from '../../lib/Constraints';
import { fireGlobalErrors } from '../../lib/Errors';
import Dropdown from '../Dropdown/Dropdown';
import PaginationNav from '../PaginationNav/PaginationNav';
import QuestionTable from './QuestionTable';

function TranslateTab({ pageNumber, pageSize }) {
    const [pageData, setPageData] = React.useState({});
    const [forceUpdate, setForceUpdate] = React.useState(false);
    const locale = React.useContext(LocaleContext);
    const { userState } = React.useContext(UserContext);

    const setPageNumberCallback = usePageNumberCallback();
    const setPageSizeCallback = usePageSizeCallback();
    
    React.useEffect(() => {
        let params = {
            pageSize: pageSize,
            pageNum: pageNumber
        };

        let headers = new Headers();
        headers.set('Accept-Language', locale.full);

        API.get({ url: "translate", params: params, headers: headers })
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

    const deleteCallback = React.useCallback((questionId) => {
        let headers = new Headers();
        headers.set('Accept-Language', locale.full);

        API.delete({ url: `${questionId}`, headers: headers })
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
                            
                            {
                                userState.loggedIn
                                && (
                                    <Link to="/add">
                                        <div className="btn btn-primary mx-5">
                                            <FontAwesomeIcon icon={["fas", "plus"]} />
                                        </div>
                                    </Link>
                                )
                            }
                            
                        </div>

                        <div className="d-flex flex-row justify-content-end w-50">
                            <div className="input-group mb-0">
                                <div className="input-group-prepend">
                                    <span className="input-group-text">
                                        <FontAwesomeIcon icon={["fas", "search"]} />
                                    </span>
                                </div>
                                
                                <input type="text" className="form-control h-100" />
                            </div>
                        </div>

                    </div>  

                </div>

                <div className="row">
                    <div className="col-2">
                        
                    </div>

                    <div className="col-10 d-flex flex-column">
                        <QuestionTable 
                            questions={pageData.data || []} 
                            deleteCallback={deleteCallback}
                        /> 
                        {nav}
                    </div>
                </div>

            </div>
        </main>
    );
}

export default TranslateTab;