import { Component, OnInit } from '@angular/core';
import { Usuario } from 'src/app/models/Usuario';
import { UsuariosService } from 'src/app/services/usuarios.service';
import Swal from 'sweetalert2';
@Component({
  selector: 'app-crud',
  templateUrl: './crud.component.html',
  styleUrls: ['./crud.component.css'],
  providers: [ UsuariosService ]
})
export class CRUDComponent implements OnInit {
  num:number;
  documento:string;
  nombre:string;
  constructor(private user:UsuariosService) { 
    this.documento="documento";
    this.nombre="nombre";
  }

  ngOnInit() {
    this.getUser();
  }
  getUser(){
    this.user.getUsers().subscribe(res =>{
      this.user.users=res as Usuario[];
      this.user.users.forEach(user => {
       user.estado=false; 
      });
      this.num=this.user.users.length;       
    });
 
  } 
  setUser(id:any){
    let documento=this.obtnerDOM(this.documento+id).value;
    let nombre=this.obtnerDOM(this.nombre+id).value;
    if (documento.length>2) {
      this.user.user=new Usuario();
      this.user.user.documento=documento;
      this.user.user.nombre=nombre;
      let estado=this.user.users.find(user=>user.documento == documento)==null;
      console.log(estado);
      if (estado) {
        this.user.setUser().subscribe(res =>{
        let user=<Usuario>res;
        user.estado=false;  
        this.user.users.push(user); 
        this.obtnerDOM(this.nombre+id).value="";
        this.obtnerDOM(this.documento+id).value="";
        this.num=this.user.users.length;
        Swal.fire(
          'agregado!',
          'has agregado un usuario!',
          'success'
        )
      });
      } else {
        this.obtnerDOM(this.documento+id).value="";
        Swal.fire(
          'error!',
          'este usuario ya existe.',
          'error'
        )
      }  
    }else{
      Swal.fire(
        'alerta!',
        'los campos deben tener una longitud minima de 3 caracteres.',
        'warning'
      )
    }

     
  }
  putUser(id: number){  
   
    let nombre=this.obtnerDOM(this.nombre+id).value;
    if (nombre.length>2) {
       Swal.fire({
        title: 'estas seguro?',
        text: "se editara el usuario existente",
        type: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Si, edita esto!',
        cancelButtonText:'cancelar'
      }).then((result) => {
        if (result.value) {   
          let documento=this.obtnerDOM(this.documento+id).value;
          this.user.user=new Usuario();
          this.user.user.documento=documento;
          this.user.user.nombre=nombre;
          this.user.putUser().subscribe(res=>{ 
            this.user.users[id]=this.user.user;
            this.user.users[id].estado=false;
          // this.getUser(); 
            Swal.fire(
              'editado!',
              'has editado al usuario.',
              'success'
            )
          }); 
        }
      })
    } else {
      Swal.fire(
        'alerta!',
        'los campos deben tener una longitud minima de 3 caracteres.',
        'warning'
      )
    }
     
  }
  deleteUser(id:number){
    Swal.fire({
      title: 'estas seguro?',
      text: "se eliminara el usuario existente.",
      type: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Si, elimina esto!',
      cancelButtonText:'cancelar'
    }).then((result) => {
      if (result.value) {
        this.user.user=this.user.users[id];
        this.user.deleteUSer().subscribe(res=>{
          this.getUser();

          Swal.fire(
            'Eliminado!',
            'has eliminado al usuario.',
            'success'
          )
        }); 
      }
    })
    
    
  }
  obtnerDOM(id :string){
   return (<HTMLInputElement>document.getElementById(id));
  }
  editar(id: number){
   
    this.user.users[id].estado=!this.user.users[id].estado;
    
  }
}
