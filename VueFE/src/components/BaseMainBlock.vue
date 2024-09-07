<template>
  <div class="base-main-container-block" @mousemove="handleMouseMove" @mouseleave="handleMouseLeave">
    <div class="hover-effect" ref="hoverEffect"></div>
    <div class="base-main-header-block">
      <slot name="header">
        <h1>S T R A N G E</h1>
      </slot>
      <div class="base-main-buttons-block">
        <button class="grey-button" @click="handleButtonClick">Add</button>
        <Dropdown :items="dropdownItems" @item-selected="onDropdownItemSelected" />
      </div>
    </div>
    <div class="content-block custom-scrollbar">
      <slot></slot>
    </div>
    <InsertNameForm v-if="showInsertNameForm" @submit="handleInsertNameSubmit" @close="closeInsertNameForm" />
  </div>
</template>
<script>
import hoverEffect from '/src/utils/hoverEffect.js';
import Dropdown from './Dropdown.vue';
import InsertNameForm from './InsertNameForm.vue';

export default {
  components: {
    Dropdown,
    InsertNameForm
  },
  mixins: [hoverEffect],
  props: {
    actionType: {
      type: String,
      default: 'space' // Default value if not specified
    }
  },
  data() {
    return {
      dropdownItems: [
        { label: 'Option 1', value: 'option1' },
        { label: 'Option 2', value: 'option2' },
        { label: 'Option 3', value: 'option3' },
        { label: 'Option 4', value: 'option4' },
        { label: 'Option 5', value: 'option5' },
        { label: 'Option 6', value: 'option6' } // Example items for dropdown
      ],
      showInsertNameForm: false // State to show/hide the form
    };
  },
  methods: {
    handleButtonClick() {
      this.showInsertNameForm = true; // Show the form
    },
    handleInsertNameSubmit(name) {
      if (!name) return;

      if (this.actionType === 'space') {
        console.log("capito da space");
        this.$emit('add-new-space', name); // Emit event to create a new space
      } else if (this.actionType === 'folder') {
        console.log("capito da FOLDER" + name);
        this.$emit('add-new-folder', name); // Emit event to create a new folder
      }

      this.showInsertNameForm = false; // Hide the form after submission
    },
    closeInsertNameForm() {
      this.showInsertNameForm = false; // Hide the form
    },
    onDropdownItemSelected(value) {
      console.log('Selected:', value);
    }
  }
};
</script>
<style scoped>
@import '@/styles/scrollbar.css';

.base-main-container-block {
  background-color: var(--menu-primary-color);
  position: fixed;
  top: 6rem;
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
  margin: 0 0.7vw;
  margin-top: 3rem;
  height: 70%;
  max-width: calc(100% - 1.4vw);
  z-index: 1;
  box-sizing: border-box;
  transition: padding 0.3s ease, margin 0.3s ease;
}

.content-block {
  max-height: 60vh; /* Imposta l'altezza massima del blocco */
  overflow-y: auto; /* Abilita lo scroll verticale quando necessario */
  height: 80%;
  width: 100%; 
  margin-top: 1rem;
  padding: 1rem;
  background-color: rgba(0, 0, 0, 0.5);
  border-radius: 10px;
  color: white;
  box-sizing: border-box;
}

.base-main-header-block {
  width: 100%;
  padding: 1rem;
  color: var(--menu-title-color);
  display: flex;
  align-items: center;
  box-sizing: border-box;
}

.base-main-header-block h1 {
  margin-left: 1rem;
  letter-spacing: 2.2rem;
  padding: 0 1rem;
  white-space: nowrap;
}

.base-main-buttons-block {
  margin-left: 1rem;
}

.base-main-header-block button,
.base-main-header-block .dropdown,
.base-main-header-block .checkbox-dropdown {
  margin: 1rem;
}

@media (max-width: 768px) {
  .base-main-container-block {
    padding: 0.5rem;
    max-width: 100%;
    margin-top: 13vw;
  }

  .base-main-header-block {
    flex-direction: column;
    align-items: center;
    padding: 0.5rem;
    text-align: center;
  }

  .base-main-buttons-block {
    margin-left: 0;
  }
}
</style>
