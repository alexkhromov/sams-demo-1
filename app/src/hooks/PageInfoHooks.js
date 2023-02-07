import React from 'react';
import PageInfoDispatchContext from '../contexts/PageInfoDispatchContext';

export function usePageNumberCallback() {
    const dispatchPageParams = React.useContext(PageInfoDispatchContext);

    return React.useCallback((pageNumber) => {
        dispatchPageParams({
            type: 'pageNumber',
            pageNumber: pageNumber
        });
    }, [dispatchPageParams]); 
}

export function usePageSizeCallback() {
    const dispatchPageParams = React.useContext(PageInfoDispatchContext);

    return React.useCallback((pageSize) => {
        dispatchPageParams({
            type: 'pageSize',
            pageSize: pageSize
        });
    }, [dispatchPageParams]);
}

export function useFilterCallback() {
    const dispatchPageParams = React.useContext(PageInfoDispatchContext);

    return React.useCallback((filter) => {
        dispatchPageParams({
            type: 'filter',
            filter: filter
        });
    }, [dispatchPageParams]);
}
