// hoverEffect.js
export default {
  methods: {
    handleMouseMove(event) {
      const element = event.currentTarget;
      const hoverEffect = this.$refs.hoverEffect;
      const { left, top } = element.getBoundingClientRect();
      const x = event.clientX - left;
      const y = event.clientY - top;
      
      hoverEffect.style.left = `${x - 50}px`; // Centra l'effetto
      hoverEffect.style.top = `${y - 50}px`;
      hoverEffect.style.transform = `scale(1)`;
      hoverEffect.style.opacity = `1`;
    },
    handleMouseLeave() {
      const hoverEffect = this.$refs.hoverEffect;
      hoverEffect.style.transform = `scale(0)`;
      hoverEffect.style.opacity = `0`;
    }
  }
};
