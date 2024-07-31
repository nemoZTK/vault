<template>
    <div class="base-main-container-block" @mousemove="handleMouseMove" @mouseleave="handleMouseLeave">
        <div class="hover-effect" ref="hoverEffect"></div>
        <div class="base-main-header-block">
            <button class="grey-button" @click="onFirstButtonClick">aggiungi</button>
            <slot name="header"> <!-- Slot per l'intestazione -->
                <h1>S T R A N G E</h1> <!-- Valore predefinito per l'intestazione -->
            </slot>
            <Dropdown :items="dropdownItems" @item-selected="onDropdownItemSelected" />
        </div>
        <div class="content-block">
            <slot> <!-- Slot per il contenuto -->
                <!-- Contenuto predefinito se non specificato -->
            </slot>
        </div>
    </div>
</template>

<script>
import hoverEffect from '/src/utils/hoverEffect.js';
import Dropdown from './Dropdown.vue'; // Assicurati che il percorso sia corretto

export default {
    components: {
        Dropdown
    },
    mixins: [hoverEffect],
    data() {
        return {
            dropdownItems: [
                { label: 'Option 1', value: 'option1' },
                { label: 'Option 2', value: 'option2' },
                { label: 'Option 3', value: 'option3' },
                { label: 'Option 4', value: 'option4' },
                { label: 'Option 5', value: 'option5' },
                { label: 'Option 6', value: 'option6' }
            ]
        };
    },
    methods: {
        onFirstButtonClick() {
            // Azione per il primo pulsante
        },
        onDropdownItemSelected(value) {
            // Azione per l'elemento selezionato nel dropdown
            console.log('Selected:', value);
        }
    }
};
</script>


<script>
import hoverEffect from '/src/utils/hoverEffect.js';
import Dropdown from './Dropdown.vue'; // Assicurati che il percorso sia corretto

export default {
    components: {
        Dropdown
    },
    mixins: [hoverEffect],
    data() {
        return {
            items: [], // Puoi inizializzare con una lista di elementi
            dropdownItems: [
                { label: 'Option 1', value: 'option1' },
                { label: 'Option 2', value: 'option2' },
                { label: 'Option 3', value: 'option3' },
                 { label: 'Option 4', value: 'option4' },
                  { label: 'Option 5', value: 'option5' },
                   { label: 'Option 6', value: 'option6' }
            ]
        };
    },
    methods: {
        onFirstButtonClick() {
            // Azione per il primo pulsante
        },
        onDropdownItemSelected(value) {
            // Azione per l'elemento selezionato nel dropdown
            console.log('Selected:', value);
        }
    }
};
</script>




<style scoped>
.base-main-container-block {
    background-color: var(--menu-primary-color);
    position: fixed;
    top: 10rem;
    left: 0;
    right: 0;
    display: flex;
    flex-direction: column;
    align-items: center;
    color: white;
    overflow: hidden;
    padding: 0.5rem 1rem;
    backdrop-filter: blur(40px);
    border-radius: 10px;
    box-shadow: 0 4px 3px rgba(0, 0, 0, 0.2);
    z-index: 1000;
    margin: 0 0.7vw;
    max-width: calc(100% - 1.4vw);
    transition: padding 0.3s ease, margin 0.3s ease;

}

.base-main-header-block {
    width: 100%;
    padding: 1rem;
    color: var(--menu-title-color);
    display: flex;
    justify-content: space-between;
    /* Distribuisce lo spazio tra gli elementi */
    align-items: center;
}

.base-main-header-block h1 {
    margin: 0;
    letter-spacing: 1.2rem;
    /* Spazio iniziale tra le lettere */
    padding: 0 1rem;
    white-space: nowrap;
    /* Impedisce al testo di andare a capo */
}

.base-main-header-block button {
    margin-left: 15rem;
}

.base-main-header-block .dropdown {
    margin-right: 15rem;
    /* Sposta il dropdown verso il lato destro */
}

/* Media queries per gestire il letter-spacing in base alla larghezza della finestra */
@media (max-width: 768px) {
    .base-main-header-block h1 {
        letter-spacing: 0.5rem;
    }
    .base-main-header-block button {
    margin-left: 0.1rem;
}

}




.content-block {
    width: 99%;
    margin-top: 1rem;
    padding: 1rem;
    background-color: rgba(0, 0, 0, 0.5);
    border-radius: 10px;
    color: white;
}

ul {
    list-style-type: none;
    padding: 0;
}

li {
    margin: 0.5rem 0;
}
</style>