import React from 'react';
import classNames from 'classnames';
import { useTranslation } from 'react-i18next';

function TabPanel({ tabIndex, tabTitles, clickTabCallback }) {
    const { t } = useTranslation('tabs');

    return (
        <div className="container-fluid mx-5 border-bottom border-primary">
            {tabTitles.map((value, index) => {
                let btnClass = classNames('btn', 'mx-2',
                    {
                        'btn-primary': index === tabIndex,
                        'btn-light': !(index === tabIndex)
                    }
                );
            
                return (
                    <button 
                        className={btnClass} 
                        onClick={()=>{clickTabCallback(index)}}
                        key={index}
                    >
                        { t(value) }
                    </button>
                );    
            })}
        </div>
    );
}

export default TabPanel;