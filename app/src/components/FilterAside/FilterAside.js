import React from 'react';
import { useTranslation } from 'react-i18next';
import FilterButton from '../FilterButton/FilterButton';

function FilterAside({ filters, currentFilter, setFilterCallback }) {
    const { t } = useTranslation('table');

    return (
        <div className="d-flex justify-content-start flex-column mx-0">
            {filters.map((value) => {
                return (
                    <FilterButton
                        filter={value.filter}
                        key={value.filter}
                        currentFilter={currentFilter}
                        onClick={setFilterCallback}
                    >
                        { t(`filter.${value.text}`) }
                    </FilterButton>
                );
            })}
        </div>
    );
}

export default FilterAside;