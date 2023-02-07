import React from 'react';

function Dropdown({title, children}) {
    const [active, setActive] = React.useState(false);
    const style = {
        top: "auto",
        display: (active ? "block" : "none")
    }

    return (
        <div className="dropdown d-inline-block">
            
            <button 
                className="btn btn-default dropdown-toggle mx-1 text-info font-weight-bold align-baseline"
                onClick={() => {setActive(!active)}}
            >
                {title}
            </button>

            <div 
                className="dropdown-menu" 
                style={style}
                onClick={() => {setActive(false)}}
            >
                {children}
            </div>
            
        </div>
    );
}

export default Dropdown;