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
    position: absolute;
    top: 10rem;
    left: 0;
    right: 0;
    display: flex;
    flex-direction: column;
    align-items: center;
    color: white;
    overflow: hidden; /* Assicura che il contenuto non esca dal contenitore */
    padding: 0.5rem 1rem;
    backdrop-filter: blur(40px);
    border-radius: 10px;
    box-shadow: 0 4px 3px rgba(0, 0, 0, 0.2);
    margin: 0 0.7vw;
    margin-top: 3rem;
    max-width: calc(100% - 1.4vw);
    z-index: 1;
    box-sizing: border-box;
    transition: padding 0.3s ease, margin 0.3s ease;
}

.base-main-header-block {
    width: 100%;
    padding: 1rem;
    color: var(--menu-title-color);
    display: flex;
    justify-content: space-between;
    align-items: center;
    box-sizing: border-box;
}

.base-main-header-block h1 {
    margin: 0;
    letter-spacing: 1.2rem;
    padding: 0 1rem;
    white-space: nowrap;
}

.base-main-header-block button {
    box-sizing: border-box;
}

.base-main-header-block .dropdown {
    box-sizing: border-box;
}

/* Media queries per gestire il layout su schermi piccoli */
@media (max-width: 768px) {
    .base-main-container-block {
        padding: 0.5rem; /* Riduci il padding per adattare meglio su schermi piccoli */
        max-width: 100%; /* Assicura che la larghezza del contenitore non superi il 100% della finestra */
        margin-top: 13vw; /* Aggiusta il margine superiore per uno spazio adeguato */
    }

    .base-main-header-block {
        flex-direction: column; /* Cambia la direzione dei flex items per adattarsi meglio sui dispositivi mobili */
        align-items: center; /* Allinea gli elementi al centro */
        padding: 0.5rem; /* Riduci il padding per più spazio sui dispositivi mobili */
        text-align: center; /* Allinea il testo al centro per una migliore leggibilità */
    }

    .base-main-header-block h1 {
        letter-spacing: 0.2rem;
        padding: 0; /* Rimuovi il padding per evitare overflow */
        margin-bottom: 0.5rem; /* Aggiungi spazio sotto l'intestazione per separarla dai pulsanti */
    }

    .base-main-header-block button,
    .base-main-header-block .dropdown {
        margin: 0.5rem 0; /* Aggiungi uno spazio verticale tra il pulsante e il dropdown */
        width: 100%; /* Assicura che i pulsanti e il dropdown occupino tutta la larghezza disponibile */
    }

    .base-main-header-block button {
        margin-bottom: 0; /* Rimuovi il margine inferiore se non necessario */
    }

    .base-main-header-block .dropdown {
        margin-bottom: 0; /* Rimuovi il margine inferiore se non necessario */
    }
}
.content-block {
    width: 100%; /* Assicurati che il contenuto blocchi si adatti alla larghezza del contenitore */
    margin-top: 1rem;
    padding: 1rem;
    background-color: rgba(0, 0, 0, 0.5);
    border-radius: 10px;
    color: white;
    box-sizing: border-box; /* Assicura che il padding e il margine non facciano superare la larghezza del contenitore */
}

</style>