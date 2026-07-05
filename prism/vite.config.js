import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'
import fs from 'fs'

const startupBannerPlugin = () => ({
  name: 'startup-banner',
  configureServer(server) {
    server.httpServer?.once('listening', () => {
      try {
        // Read the file
        const banner = fs.readFileSync('./banner.txt', 'utf-8');

        // Print it with ANSI color codes (Cyan)
        console.log(`\n\x1b[36m${banner}\x1b[0m`);

        console.log(`\x1b[33m:: Prism Frontend ::`);
      } catch (e) {

      }
    });
  }
});

// https://vite.dev/config/
export default defineConfig({
  plugins: [
    vue(),
    vueDevTools(),
    startupBannerPlugin()
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url)),
      '@components': fileURLToPath(new URL('./src/components', import.meta.url))
    },
  },
})
