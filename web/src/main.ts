import {enableProdMode} from '@angular/core';
import {platformBrowserDynamic} from '@angular/platform-browser-dynamic';

import {AppModule} from './app/app.module';
import {environment} from './environments/environment';
import {Urls} from './app/services/api-services.service';

if (environment.production) {
  enableProdMode();
}

platformBrowserDynamic().bootstrapModule(AppModule)
  .catch(err => console.error(err));


console.log('\n' +
  '    __                    \n' +
  '   / /__  _________  ____ \n' +
  '  / / _ \\/ ___/ __ \\/ __ \\\n' +
  ' / /  __/ /__/ /_/ / / / /\n' +
  '/_/\\___/\\___/\\____/_/ /_/ \n' +
  '                          \n');
