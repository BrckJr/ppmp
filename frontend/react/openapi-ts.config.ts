import { defineConfig } from '@hey-api/openapi-ts';

export default defineConfig({
  client: '@hey-api/client-axios',
  input: '../../backend/app/target/openapi/openapi.json',
  output: './src/api/generated',
});