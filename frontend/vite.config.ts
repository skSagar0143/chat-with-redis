import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
  plugins: [react()],
  define: {
    // SockJS expects Node's `global`; map it to the browser global object.
    global: 'globalThis',
  },
  server: { port: 5173 },
})
