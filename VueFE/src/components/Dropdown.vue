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
  .dropdown-content div:hover {
    background: var(--menu-btn-hover-color);
  }
  </style>
  