<template>
    <BaseForm title="Filtri" submitButtonText="Applica" :isVisible="isVisible" @submit="applyFilters" @close="hideForm">
      <template v-slot:form-fields>
        <div class="filter-group">
          <h3>Filtri</h3>
          <div v-for="(filter, index) in filters" :key="index">
            <label>
              <input type="checkbox" :value="filter.value" v-model="selectedFilters" />
              {{ filter.label }}
            </label>
            <div v-if="selectedFilters.includes(filter.value)">
              <h4>Sottofiltri</h4>
              <div v-for="(subfilter, subIndex) in filter.subfilters" :key="subIndex">
                <label>
                  <input type="checkbox" :value="subfilter.value" v-model="selectedSubfilters" />
                  {{ subfilter.label }}
                </label>
              </div>
            </div>
          </div>
        </div>
      </template>
      <template v-slot:footer>
        <p>Seleziona i filtri e sottofiltri desiderati.</p>
      </template>
    </BaseForm>
  </template>
  
  <script>
  import BaseForm from './BaseForm.vue';
  
  export default {
    components: { BaseForm },
    props: {
      isVisible: {
        type: Boolean,
        default: false
      }
    },
    data() {
      return {
        filters: [
          { label: 'Filtro 1', value: 'filter1', subfilters: [{ label: 'Sottofiltro 1.1', value: 'subfilter1.1' }, { label: 'Sottofiltro 1.2', value: 'subfilter1.2' }] },
          { label: 'Filtro 2', value: 'filter2', subfilters: [{ label: 'Sottofiltro 2.1', value: 'subfilter2.1' }, { label: 'Sottofiltro 2.2', value: 'subfilter2.2' }] }
        ],
        selectedFilters: [],
        selectedSubfilters: []
      };
    },
    methods: {
      applyFilters() {
        this.$emit('filters-applied', { filters: this.selectedFilters, subfilters: this.selectedSubfilters });
        this.$emit('close');
      },
      hideForm() {
        this.$emit('close');
      }
    }
  };
  </script>
  
  <style scoped>
  /* Assicurati che il filtro occupi un overlay sopra tutto */
  .filter-group {
    margin-bottom: 1rem;
  }
  
  /* Assicurati che il modulo occupi la giusta area e sia centrato */
  .form-container {
    max-width: 600px; /* Aggiungi larghezza per un aspetto consistente */
  }
  </style>
  