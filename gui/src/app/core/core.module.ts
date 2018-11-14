import { NgModule } from '@angular/core';
import { CoreRoutingModule } from './core-routing.module';
import { RouterModule, RouterOutlet } from '@angular/router';
import { HeaderComponent } from './layout/header/header.component';
import { SharedModule } from '../shared/components/shared.module';
import { LoaderService } from '../shared/services/loading.service';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { LoaderInterceptor } from '../shared/services/loaderinterceptor';

@NgModule({
    declarations: [
        HeaderComponent
    ],
    exports: [
        HeaderComponent
    ],
    imports: [
        RouterModule,
        CoreRoutingModule,
        SharedModule
    ],
    providers: [
        LoaderService,
        {
            provide: HTTP_INTERCEPTORS,
            useClass: LoaderInterceptor,
            multi: true
        }
    ]
})
export class CoreModule { }

export * from '../shared/services/loading.service'