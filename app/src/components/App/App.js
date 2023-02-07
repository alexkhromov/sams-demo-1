import React from 'react';
import { useTranslation } from 'react-i18next';
import { HashRouter, Route, Switch } from 'react-router-dom';
import UserEdit from '../../components/UserEdit/UserEdit';
import LocaleContext from '../../contexts/LocaleContext';
import PageInfoDispatchContext from '../../contexts/PageInfoDispatchContext';
import UserContext from '../../contexts/UserContext';
import { LOCALE, ROLE, TABS } from '../../lib/Constraints';
import JWT from '../../lib/JWT';
import { usePageInfoReducer } from '../../reducers/PageInfoReducer';
import { useUserReducer } from '../../reducers/UserReducer';
import QuestionAdd from '../QuestionAdd/QuestionAdd';
import QuestionEdit from '../QuestionEdit/QuestionEdit';
import QuestionsTab from '../QuestionsTab/QuestionsTab';
import Search from '../Search/Search';
import SignIn from '../SignIn/SignIn';
import SignUp from '../SignUp/SignUp';
import TabPanel from '../TabPanel/TabPanel';
import TranslateTab from '../TranslateTab/TranslateTab';
import UsersTab from '../UsersTab/UsersTab';
import './App.css';
import AppNavbar from './AppNavbar';

function App() {
    const [pageInfoState, pageInfoDispatch] = usePageInfoReducer();
    const [userState, userDispatch] = useUserReducer();

    const [locale, setLocale] = React.useState(LOCALE.EN);
    const { t, i18n } = useTranslation();

    const changeLangCallback = React.useCallback((locale) => {
        i18n.changeLanguage(locale.short);
        setLocale(locale);
    }, [i18n]);

    React.useEffect(() => {
        let token = JWT.getStorage();

        if (token) {
            userDispatch({ type: 'signin', token: token })
        }

        if (JWT.tokenExpired()) {
            userDispatch({ type: 'signout' });
        }

        let interval = setInterval(() => {
            if (JWT.tokenExpired()) {
                userDispatch({ type: 'signout' });
            }
        }, 30 * 60 * 1000);
        
        return () => {
            clearInterval(interval);
        }
    }, [userDispatch]);

    const clickTabCallback = React.useCallback((index) => {
        pageInfoDispatch({ type: 'tabIndex', tabIndex: index });
    }, [pageInfoDispatch]);

    let tabs = [];

    tabs.push({
        title: TABS.USER,
        component: (
            <QuestionsTab 
                pageNumber={pageInfoState.pageNumber}
                pageSize={pageInfoState.pageSize}
                filter={pageInfoState.filter}
            />
        )
    });

    if (userState.roles.includes(ROLE.ADMIN)) {
        tabs.push({
            title: TABS.ADMIN,
            component: (
                <UsersTab
                    pageNumber={pageInfoState.pageNumber}
                    pageSize={pageInfoState.pageSize}
                />
            )
        });
    }

    if (userState.roles.includes(ROLE.TRANSLATOR)) {
        tabs.push({
            title: TABS.TRANSLATOR,
            component: (
                <TranslateTab
                    pageNumber={pageInfoState.pageNumber}
                    pageSize={pageInfoState.pageSize}
                />
            )
        })
    }

    const isTabbed = tabs.length > 1;

    React.useEffect(() => {
        pageInfoDispatch({ type: 'tabIndex', tabIndex: 0 });
    }, [isTabbed, pageInfoDispatch]);

    return (
        <HashRouter>
            <UserContext.Provider value={{userState: userState, userDispatch: userDispatch}}>
                <div className="App">

                    <AppNavbar 
                        currentLang={locale.full}
                        changeLangCallback={changeLangCallback}
                    />

                    <div className="content">
                        <PageInfoDispatchContext.Provider value={pageInfoDispatch}>
                            <LocaleContext.Provider value={locale}>
                                <Switch>
                                    <Route exact path="/add">
                                        <QuestionAdd />
                                    </Route>

                                    <Route 
                                        path="/edit/:questionId" 
                                        component={QuestionEdit}
                                    />

                                    <Route
                                        path="/edit-user/:userId"
                                        component={UserEdit}
                                    />

                                    <Route
                                        path="/search/:query"
                                        children={({ match }) => (
                                            <Search
                                                pageNumber={pageInfoState.pageNumber}
                                                pageSize={pageInfoState.pageSize}
                                                match={match}
                                            />
                                        )}
                                    />

                                    <Route 
                                        exact path="/signin"
                                        component={SignIn}
                                    />

                                    <Route
                                        exact path="/signup"
                                        component={SignUp}
                                    />

                                    <Route exact path="/">

                                        {isTabbed
                                            && (
                                                <TabPanel
                                                    tabIndex={Math.min(pageInfoState.tabIndex, tabs.length)}
                                                    tabTitles={tabs.map((value) => value.title)}
                                                    clickTabCallback={clickTabCallback}
                                                />
                                            )
                                        }
                                        {tabs.filter((value, index) => index === Math.min(pageInfoState.tabIndex, tabs.length))[0].component}
                                        
                                    </Route>
                                </Switch>
                            </LocaleContext.Provider>
                        </PageInfoDispatchContext.Provider>
                    </div>

                    <footer className="footer mt-5">
                        <div className="bg-light d-flex flex-column align-items-center justify-content-center w-100">
                            <span>© {t('main.author')} </span>
                            <span>hrom1981@gmail.com</span>
                        </div>
                    </footer>

                </div>
            </UserContext.Provider>
        </HashRouter>
    );
}

export default App;
