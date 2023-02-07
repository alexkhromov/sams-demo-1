import React from 'react';
import classNames from 'classnames';

function FilterButton({ filter, currentFilter, onClick, className, children }) {
    let btnClass = classNames('btn', 
        {
            'btn-primary': filter === currentFilter,
            'btn-light': !(filter === currentFilter)
        },
        className
    );
    
    return (
        <button
            onClick={() => {onClick(filter)}}
            className={btnClass}
        >
            {children}
        </button>
    );
}

export default FilterButton;