<template>
    <div class="dropdown" @click="toggleDropdown">
      <button class="dropdown-button">{{ selectedItem }}</button>
      <div v-if="isOpen" class="dropdown-content custom-scrollbar">
        <div v-for="item in items" :key="item.value" @click="selectItem(item)">
          {{ item.label }}
        </div>

      </div>
    </div>
  </template>
  
  <script>
  export default {
    props: {
      items: {
        type: Array,
        required: true
      },
      defaultValue: {
        type: String,
        default: 'Order By'
      }
    },
    data() {
      return {
        isOpen: false,
        selectedItem: this.defaultValue
      };
    },
    methods: {
      toggleDropdown() {
        this.isOpen = !this.isOpen;
      },
      selectItem(item) {
        this.selectedItem = item.label;
        this.isOpen = false;
        this.$emit('item-selected', item.value);
      }
    }
  };
  </script>
  
  <style scoped>
.dropdown {
  position: relative;
  display: inline-block;
}

.dropdown-button {
  padding: 0.5rem 1rem;
  background: var(--menu-btn-color);
  border: none;
  border-radius: 5px;
  color: var(--menu-title-color);
  cursor: pointer;
  font-size: 1rem;
}

.dropdown-button:hover {
  background: var(--menu-btn-hover-color);
}

.dropdown-content {
  display: flex;
  flex-direction: column;
  position: absolute;
  background-color: rgba(0, 0, 0, 0.064); /* Colore scuro come content-block */
  border-radius: 5px;
  box-shadow: 0 4px 3px rgba(0, 0, 0, 0.352);
  backdrop-filter: blur(10px);
  top: 100%;
  left: 0;
  right: 0;
  z-index: 1000;
  
  white-space: nowrap;
  max-height: 100px; /* Imposta l'altezza massima della tendina */
  overflow-y: auto; /* Abilita lo scroll quando il contenuto supera l'altezza massima */
}

.dropdown-content div {
  padding: 0.5rem 1rem;
  color: var(--menu-title-color);
  cursor: pointer;
}

.dropdown-content div:hover {
  background: var(--menu-btn-hover-color);
}
  </style>
  