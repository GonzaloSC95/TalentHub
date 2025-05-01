import { Routes } from '@angular/router';
import { VacanteDetalleComponent } from './components/vacante-detalle/vacante-detalle.component';
import { HomeComponent } from './pages/home/home.component';
import { LandingComponent } from './pages/landing/landing.component';
import { LoginComponent } from './pages/login/login.component';
import { RegistroComponent } from './pages/registro/registro.component';

import { ModificarComponent } from './components/modificar/modificar.component';



export const routes: Routes = [
  { path: '', redirectTo: 'home', pathMatch: 'full' },
  { path: 'home', component: HomeComponent },
  { path: 'login', component: LoginComponent },
  { path: 'registro', component: RegistroComponent },
  { path: 'landing/:email', component: LandingComponent},
  { path: 'admin/list/:type', component :LandingComponent},
  { path: 'user/list/:type', component :LandingComponent},
  //{ path: 'vacante/detalle/:id', component: VacanteDetalleComponent },
  { path: 'detalle/:type/:param', component: VacanteDetalleComponent },
  //{ path: 'modificar/:tipo/:id', component: VacanteModificarComponent  }
  { path: 'modificar2/:tipo/:id', component: ModificarComponent }

];
