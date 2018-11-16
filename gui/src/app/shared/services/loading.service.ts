import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ReplaySubject } from 'rxjs';
import { BehaviorSubject } from 'rxjs';

// Code from: https://stackblitz.com/edit/angular-o3my4y?file=app%2Fapp.component.ts
@Injectable()
export class LoaderService {
    public isLoading = new BehaviorSubject(false);

    constructor() {}
}


