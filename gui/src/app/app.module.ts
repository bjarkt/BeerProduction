import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { CoreComponent } from './core/core.component';
import { CoreModule } from './core/core.module';
import { RouterModule } from '@angular/router';
import { SharedModule } from './shared/components/shared.module';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HttpClientModule } from '@angular/common/http';
import { ServiceWorkerModule } from '@angular/service-worker';
import { environment } from '../environments/environment';

@NgModule({
  declarations: [
    CoreComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    CoreModule,
    RouterModule,
    SharedModule,
    BrowserAnimationsModule,
    ServiceWorkerModule.register('ngsw-worker.js', { enabled: environment.production })
  ],
  providers: [],
  bootstrap: [CoreComponent]
})
export class AppModule { }
