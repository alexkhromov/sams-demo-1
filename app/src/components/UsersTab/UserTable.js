import React from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useTranslation } from 'react-i18next';
import { Link } from 'react-router-dom';
import UserContext from '../../contexts/UserContext';
import { ROLE } from '../../lib/Constraints';

function UserTable({ users, deleteCallback }) {
    const { t } = useTranslation('table');
    const { userState } = React.useContext(UserContext);

    return (
        <table className="table table-fixed">
            <thead>
                <tr>
                    <th scope="col" style={{width: "30%"}}>
                        { t('header.email') }
                    </th>
                    <th scope="col" style={{width: "30%"}}>
                        { t('header.username') }
                    </th>
                    <th scope="col" style={{width: "30%"}}>
                        { t('header.roles') }
                    </th>
                    <th scope="col" style={{width: "10%"}}>
                        
                    </th>
                </tr>
            </thead>
            <tbody>
                {users.map((user) => {
                    return (
                        <tr key={user.id.toString()}>
                            <td>
                                <div className="tooltip-wrapper">
                                    <div className="text-truncate">
                                        {user.email}
                                    </div>

                                    <span className="tooltip-text text-break">
                                        {user.email}
                                    </span>
                                </div>
                            </td>

                            <td>
                                <div className="tooltip-wrapper">
                                    <div className="text-truncate">
                                        {user.username}
                                    </div>

                                    <span className="tooltip-text text-break">
                                        {user.username}
                                    </span>
                                </div>
                            </td>

                            <td>
                                {user.roles.map((role) => t(`role.${role}`)).join(', ')}
                            </td>

                            <td>
                                {
                                    userState.loggedIn 
                                    && userState.roles.includes(ROLE.ADMIN)
                                    && (
                                        <Link to={`/edit-user/${user.id}`}>
                                            <FontAwesomeIcon icon={["far", "edit"]} className="text-info mx-2" />
                                        </Link>
                                    )
                                }

                                {
                                    userState.loggedIn 
                                    && userState.roles.includes(ROLE.ADMIN)
                                    && (
                                        <a 
                                            className="text-danger mx-2" 
                                            href="#"
                                            onClick={() => {deleteCallback(user.id)}}
                                        >
                                            <FontAwesomeIcon icon={["far", "trash-alt"]} />
                                        </a>
                                    )
                                }
                            </td>
                        </tr>
                    );
                })}
            </tbody>
        </table>
    );
}

export default UserTable;