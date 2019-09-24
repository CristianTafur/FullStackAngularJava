import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Usuario } from '../models/Usuario'; 

@Injectable({
  providedIn: 'root'
})
export class UsuariosService {
  users:Usuario[];
  user:Usuario;
  url:string='http://localhost:8080/AR/web/generic/usuarios';
  constructor(private http:HttpClient) { 
    this.user= new Usuario();
  }
  getUsers(){
    return this.http.get(this.url);
  }
  setUser(){
    
    return this.http.get(this.url+'/insetar/'+this.user.documento+'/'+this.user.nombre);
  }
  putUser(){
    return this.http.get(this.url+ `/editar/${this.user.documento}/${this.user.nombre}`);
  }
  deleteUSer(){
    
    return this.http.get(this.url+ `/eliminar/${this.user.documento}`);
  }
}
