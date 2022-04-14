package ma.emsi.jeetp3.web;

import lombok.AllArgsConstructor;
import ma.emsi.jeetp3.entities.Patient;
import ma.emsi.jeetp3.repositories.PatientRepository;
import org.hibernate.boot.jaxb.spi.Binding;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.awt.print.Pageable;
import java.util.List;
@Controller
@AllArgsConstructor
public class PatientController {
    private PatientRepository patientRepository;
    @GetMapping (path="/user/index")
   public String patients(Model model ,
                          @RequestParam(name ="page",defaultValue = "0") int page,
                          @RequestParam(name ="size",defaultValue = "5")int size,
                          @RequestParam(name ="keyword",defaultValue = "") String keyword
    ){
        Page<Patient> pagepatients =patientRepository.findByNomContains((keyword), PageRequest.of(page,size));
        model.addAttribute( "listPatients",pagepatients.getContent());
        model.addAttribute("pages",new int[pagepatients.getTotalPages()]);
        model.addAttribute("currentPage",page);
        model.addAttribute("keyword",keyword);
     return "patients";
   }
   @GetMapping("/admin/delete")
   public String delete(Long id,String keyword,int page){
       patientRepository.deleteById(id);
       return "redirect:/user/index?page"+page+"&keyword"+keyword;
   }
   @GetMapping("/")
    public String home(){
       return "home";
   }
   @GetMapping("/user/patients")
   @ResponseBody
   public List<Patient>listPatients(){
       return patientRepository.findAll();
   }

   @GetMapping("/admin/formPatients")
   public String formPatients(Model model){
       model.addAttribute("patient",new Patient());
       return "formPatients";
}
@PostMapping(path = "/admin/save")
public  String save (Model model, @Valid Patient patient, BindingResult bindingResult,
                     @RequestParam(defaultValue="")String keyword, @RequestParam(defaultValue="0") int page){
       if (bindingResult.hasErrors()) return "formPatients";
       patientRepository.save(patient);
       return "redirect:/user/index?page="+page+"&keyword="+keyword;
}
    @GetMapping(path = "/admin/editPatient")
    public  String editPatient(Model model,long id , String keyword, int page){
       Patient patient=patientRepository.findById(id).orElse(null);
        if (patient==null)throw  new RuntimeException("Patient introuvable");
        model.addAttribute("patient",patient);
        model.addAttribute("page",page);
        model.addAttribute("keyword",keyword);
        return "editPatient";
    }
}
