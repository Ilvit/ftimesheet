package com.timesheet;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.timesheet.repositories.RoleRepository;
import com.timesheet.repositories.TimesheetSaverRepository;
import com.timesheet.repositories.USAIDProjectRepository;
import com.timesheet.services.AppUserService;
import com.timesheet.services.EmployeeService;
import com.timesheet.services.HolidayService;
import com.timesheet.services.SheetdayService;

@SpringBootApplication
public class TimesheetServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TimesheetServiceApplication.class, args);
	}	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean 
	CommandLineRunner clr(EmployeeService employeeService, SheetdayService sheetdayService, RoleRepository appRoleRepository,USAIDProjectRepository projectRepository,
			TimesheetSaverRepository savedSheetLineRepository, AppUserService appUserService, HolidayService holidayService) {
		return  args->{
			//Holidays
			/*
			 * holidayService.addHoliday(LocalDate.parse("lundi_18-mars-2024",
			 * TimesheetPeriods.dtf), "Faradja's USAID-ULINZI day");
			 * holidayService.addHoliday(LocalDate.parse("dimanche_30-juin-2024",
			 * TimesheetPeriods.dtf), "DRC independence day");
			 * holidayService.addHoliday(LocalDate.parse("jeudi_01-août-2024",
			 * TimesheetPeriods.dtf), "DRC day of the dead");
			 
			
			*/
			//Projects
//			projectRepository.save(new USAIDProject(null,"USAID ULINZI","Key Population project"));
//			projectRepository.save(new USAIDProject(null,"EPIC","Epidemiologic Control"));
//			
//			//Add employees
//			employeeService.addNewEmployee("SEYA", "SENEFU", "Francine","MAFA005UU", "fseya@maisonfaradja.org", Positions.RECEPTIONIST);
//			employeeService.addNewEmployee("KYUNGU", "MWANANGOY", "Tresor","MAFA013UU", "tresorkyungu@maisonfaradja.org", Positions.SITE_SUPERVISOR_DILOLO);
//			employeeService.addNewEmployee("MUCHECHE", "NYANDWE", "Jean-Paul","MAFA011UU", "jpmucheche@maisonfaradja.org", Positions.SITE_SUPERVISOR_KAPANGA);
//			employeeService.addNewEmployee("NGOIE", "MUTOMBO", "Didier","MAFA001UU", "didierngoie@maisonfaradja.org", Positions.COP);
//			employeeService.addNewEmployee("NGOY", "KABWE", "Delly","MAFA002UU", "dellyngoy@maisonfaradja.org", Positions.DCOP);
//			employeeService.addNewEmployee("KASHIKA", "MUTOMBO", "Clement","MAFA006UU", "clementkashika@maisonfaradja.org", Positions.FINANCE_ASSOCIATE);
//			employeeService.addNewEmployee("BOMASI", "GIZELA", "Michelle","MAFA012UU", "mbomasi@maisonfaradja.org", Positions.FINANCE_OFFICER);
//			employeeService.addNewEmployee("MUKUMBA", "KAMWANYA", "Curtis","MAFA003UU", "curtismukumba@maisonfaradja.org", Positions.SSI);
//			employeeService.addNewEmployee("KALWA", "TUNDWA", "Patty", "MAFA009UU","pattykalwa@maisonfaradja.org", Positions.KPTA);
//			employeeService.addNewEmployee("MBUTA", "NKANGI", "Frederic","MAFA015UU", "fredericmbuta@maisonfaradja.org", Positions.SIM);
//			employeeService.addNewEmployee("MUNONGO", "FUTILA", "Nova","MAFA004UU", "novamunongo@maisonfaradja.org", Positions.DAF);
//			employeeService.addNewEmployee("MWAMBA", "ILUNGA", "Dyese","MAFA007UU", "dyesemwamba@maisonfaradja.org", Positions.LOG_SUPPLY_CHAIN);
//			employeeService.addNewEmployee("ILUNGA", "KABWE", "Vital", "MAFA018UU","vitalilunga@maisonfaradja.org", Positions.IT_OFFICER_COMM_FP);
//			employeeService.addNewEmployee("NGOIE", "LUBOMBO", "Michée", "MAFA010UU","micheelubombo@maisonfaradja.org", Positions.LOG_ASSOCIATE);
//			employeeService.addNewEmployee("KITWA", "MUYAMPE", "Jérémie", "MAFA008UU","kitwamuyampefils@gmail.com", Positions.GARDENER_KOLWEZI);
//			employeeService.addNewEmployee("LUFULWABO", "TSHALA", "Fifi", "MAFA014UU","fifilufulwabo@gmail.com", Positions.CLEANER_KOLWEZI);
//			employeeService.addNewEmployee("SAMBA", "YAMBO", "Béatrice", "MAFA016UU","beasamba@gmail.com", Positions.CLEANER_KASAJI);
//			employeeService.addNewEmployee("NGOIE", "SAMBA", "Hervé", "MAFA017UU","ngoieherve@gmail.com", Positions.DRIVER_KOLWEZI);
//			
////			Add roles
//			for(RolesNames roleName:RolesNames.values()) {
//				appRoleRepository.save(new Role(null,roleName.name()));
//			}
//			//define a supervisor
//			
////			employeeService.setSupervisorToEmployee("NGOIE", "NGOY");
//			
//			employeeService.setSupervisor("MAFA018UU", "MAFA015UU");
//			employeeService.setSupervisor("MAFA003UU", "MAFA015UU");
//			employeeService.setSupervisor("MAFA015UU", "MAFA002UU");
//			employeeService.setSupervisor("MAFA013UU", "MAFA009UU");
//			employeeService.setSupervisor("MAFA011UU", "MAFA009UU");
//			employeeService.setSupervisor("MAFA009UU", "MAFA002UU");
//			employeeService.setSupervisor("MAFA006UU", "MAFA012UU");
//			employeeService.setSupervisor("MAFA012UU", "MAFA004UU");
//			employeeService.setSupervisor("MAFA007UU", "MAFA004UU");
//			employeeService.setSupervisor("MAFA005UU", "MAFA004UU");
//			employeeService.setSupervisor("MAFA010UU", "MAFA007UU");
//			employeeService.setSupervisor("MAFA014UU", "MAFA007UU");
//			employeeService.setSupervisor("MAFA016UU", "MAFA007UU");
//			employeeService.setSupervisor("MAFA017UU", "MAFA007UU");
//			employeeService.setSupervisor("MAFA004UU", "MAFA002UU");
//			employeeService.setSupervisor("MAFA002UU", "MAFA001UU");
//			employeeService.setSupervisor("MAFA001UU", "MAFA004UU");
//			
////			Add users
//			appUserService.saveUser(new AppUser("Vital", "12345", "MAFA018UU"));			
////			appUserService.saveUser(new AppUser("Tresor", "12345", "0002"));
////			appUserService.saveUser(new AppUser("Nova", "12345", employeeService.getEmployeeID("NOVA")));
////			appUserService.saveUser(new AppUser("Francine", "12345", employeeService.getEmployeeID("SEYA")));
////			appUserService.saveUser(new AppUser("Tresor", "12345", employeeService.getEmployeeID("KYUNGU")));
////			appUserService.saveUser(new AppUser("Jean-Paul", "12345", employeeService.getEmployeeID("MUCHECHE")));
////			appUserService.saveUser(new AppUser("Dyese", "12345", employeeService.getEmployeeID("MWAMBA")));
////			appUserService.saveUser(new AppUser("Curtis", "12345", employeeService.getEmployeeID("MUKUMBA")));
////			appUserService.saveUser(new AppUser("Vital", "123456", employeeService.getEmployeeID("ILUNGA")));
////			appUserService.saveUser(new AppUser("Clement", "12345", employeeService.getEmployeeID("KASHIKA")));
////			appUserService.saveUser(new AppUser("Michelle", "12345", employeeService.getEmployeeID("BOMASI")));
////			Add roles to users
//			appUserService.addRoleToUser("Vital", "USER");	
//			appUserService.addRoleToUser("Vital", "SUPERVISOR");	
//			appUserService.addRoleToUser("Vital", "HR");
//			appUserService.addRoleToUser("Vital", "ADMIN");	
////			appUserService.addRoleToUser("Francine", "DAF");	
////			appUserService.addRoleToUser("Tresor", "USER");	
////			appUserService.addRoleToUser("Vital", "ADMIN");
////			appUserService.addRoleToUser("Vital", "SUPERVISOR");
////			appUserService.addRoleToUser("Vital", "HR");
////			appUserService.addRoleToUser("Nova", "USER");	
////			appUserService.addRoleToUser("Nova", "SUPERVISOR");	
////			appUserService.addRoleToUser("Nova", "DAF");	
////			appUserService.addRoleToUser("Curtis", "USER");	
////			appUserService.addRoleToUser("Francine", "USER");	
////			appUserService.addRoleToUser("Dyese", "USER");	
////			appUserService.addRoleToUser("Dyese", "SUPERVISOR");	
////			appUserService.addRoleToUser("Michelle", "USER");	
////			appUserService.addRoleToUser("Clement", "USER");	
//			//Creating a timesheet line
//			TimesheetPeriods tp=new TimesheetPeriods();
//			List<Sheetday>sheetdays=sheetdayService.getNewTimesheetLine(tp.getPrecedentPeriod(tp.getCurrentPeriod()), "MAFA001UU", "", "USAID ULINZI");
//			List<Sheetday>sheetdays2=sheetdayService.getNewTimesheetLine(tp.getPrecedentPeriod(tp.getCurrentPeriod()), "MAFA002UU", "", "USAID ULINZI");
//			List<Sheetday>sheetdays3=sheetdayService.getNewTimesheetLine(tp.getPrecedentPeriod(tp.getCurrentPeriod()), "MAFA003UU", "", "USAID ULINZI");
//			List<Sheetday>sheetdays4=sheetdayService.getNewTimesheetLine(tp.getPrecedentPeriod(tp.getCurrentPeriod()), "MAFA004UU", "", "USAID ULINZI");
//			List<Sheetday>sheetdays5=sheetdayService.getNewTimesheetLine(tp.getPrecedentPeriod(tp.getCurrentPeriod()), "MAFA005UU", "", "USAID ULINZI");
//			List<Sheetday>sheetdays6=sheetdayService.getNewTimesheetLine(tp.getPrecedentPeriod(tp.getCurrentPeriod()), "MAFA006UU", "", "USAID ULINZI");
//			List<Sheetday>sheetdays7=sheetdayService.getNewTimesheetLine(tp.getPrecedentPeriod(tp.getCurrentPeriod()), "MAFA007UU", "", "USAID ULINZI");
//			List<Sheetday>sheetdays8=sheetdayService.getNewTimesheetLine(tp.getPrecedentPeriod(tp.getCurrentPeriod()), "MAFA008UU", "", "USAID ULINZI");
//			List<Sheetday>sheetdays9=sheetdayService.getNewTimesheetLine(tp.getPrecedentPeriod(tp.getCurrentPeriod()), "MAFA009UU", "", "USAID ULINZI");
//			List<Sheetday>sheetdays10=sheetdayService.getNewTimesheetLine(tp.getPrecedentPeriod(tp.getCurrentPeriod()), "MAFA010UU", "", "USAID ULINZI");
//			List<Sheetday>sheetdays11=sheetdayService.getNewTimesheetLine(tp.getPrecedentPeriod(tp.getCurrentPeriod()), "MAFA011UU", "", "USAID ULINZI");
//			List<Sheetday>sheetdays12=sheetdayService.getNewTimesheetLine(tp.getPrecedentPeriod(tp.getCurrentPeriod()), "MAFA012UU", "", "USAID ULINZI");
//			List<Sheetday>sheetdays13=sheetdayService.getNewTimesheetLine(tp.getPrecedentPeriod(tp.getCurrentPeriod()), "MAFA013UU", "", "USAID ULINZI");
//			List<Sheetday>sheetdays14=sheetdayService.getNewTimesheetLine(tp.getPrecedentPeriod(tp.getCurrentPeriod()), "MAFA014UU", "", "USAID ULINZI");
//			List<Sheetday>sheetdays15=sheetdayService.getNewTimesheetLine(tp.getPrecedentPeriod(tp.getCurrentPeriod()), "MAFA015UU", "", "USAID ULINZI");
//			List<Sheetday>sheetdays16=sheetdayService.getNewTimesheetLine(tp.getPrecedentPeriod(tp.getCurrentPeriod()), "MAFA016UU", "", "USAID ULINZI");
//			List<Sheetday>sheetdays17=sheetdayService.getNewTimesheetLine(tp.getPrecedentPeriod(tp.getCurrentPeriod()), "MAFA017UU", "", "USAID ULINZI");
//			List<Sheetday>sheetdays18=sheetdayService.getNewTimesheetLine(tp.getPrecedentPeriod(tp.getCurrentPeriod()), "MAFA018UU", "", "USAID ULINZI");
//			
//			sheetdayService.saveTimesheetLine(sheetdays);
//			sheetdayService.saveTimesheetLine(sheetdays2);
//			sheetdayService.saveTimesheetLine(sheetdays3);
//			sheetdayService.saveTimesheetLine(sheetdays4);
//			sheetdayService.saveTimesheetLine(sheetdays5);
//			sheetdayService.saveTimesheetLine(sheetdays6);
//			sheetdayService.saveTimesheetLine(sheetdays7);
//			sheetdayService.saveTimesheetLine(sheetdays8);
//			sheetdayService.saveTimesheetLine(sheetdays9);
//			sheetdayService.saveTimesheetLine(sheetdays10);
//			sheetdayService.saveTimesheetLine(sheetdays11);
//			sheetdayService.saveTimesheetLine(sheetdays12);
//			sheetdayService.saveTimesheetLine(sheetdays13);
//			sheetdayService.saveTimesheetLine(sheetdays14);
//			sheetdayService.saveTimesheetLine(sheetdays15);
//			sheetdayService.saveTimesheetLine(sheetdays16);
//			sheetdayService.saveTimesheetLine(sheetdays17);
//			sheetdayService.saveTimesheetLine(sheetdays18);
//			
//			TimesheetSaver userSavedSl=new TimesheetSaver(null, "MAFA001UU", tp.getPrecedentPeriod(tp.getCurrentPeriod()), false, false, false, false, false, false);
//			TimesheetSaver userSavedS2=new TimesheetSaver(null, "MAFA002UU", tp.getPrecedentPeriod(tp.getCurrentPeriod()), false, false, false, false, false, false);
//			TimesheetSaver userSavedS3=new TimesheetSaver(null, "MAFA003UU", tp.getPrecedentPeriod(tp.getCurrentPeriod()), false, false, false, false, false, false);
//			TimesheetSaver userSavedS4=new TimesheetSaver(null, "MAFA004UU", tp.getPrecedentPeriod(tp.getCurrentPeriod()), false, false, false, false, false, false);
//			TimesheetSaver userSavedS5=new TimesheetSaver(null, "MAFA005UU", tp.getPrecedentPeriod(tp.getCurrentPeriod()), false, false, false, false, false, false);
//			TimesheetSaver userSavedS6=new TimesheetSaver(null, "MAFA006UU", tp.getPrecedentPeriod(tp.getCurrentPeriod()), false, false, false, false, false, false);
//			TimesheetSaver userSavedS7=new TimesheetSaver(null, "MAFA007UU", tp.getPrecedentPeriod(tp.getCurrentPeriod()), false, false, false, false, false, false);
//			TimesheetSaver userSavedS8=new TimesheetSaver(null, "MAFA008UU", tp.getPrecedentPeriod(tp.getCurrentPeriod()), false, false, false, false, false, false);
//			TimesheetSaver userSavedS9=new TimesheetSaver(null, "MAFA009UU", tp.getPrecedentPeriod(tp.getCurrentPeriod()), false, false, false, false, false, false);
//			TimesheetSaver userSavedS10=new TimesheetSaver(null, "MAFA010UU", tp.getPrecedentPeriod(tp.getCurrentPeriod()), false, false, false, false, false, false);
//			TimesheetSaver userSavedS11=new TimesheetSaver(null, "MAFA011UU", tp.getPrecedentPeriod(tp.getCurrentPeriod()), false, false, false, false, false, false);
//			TimesheetSaver userSavedS12=new TimesheetSaver(null, "MAFA012UU", tp.getPrecedentPeriod(tp.getCurrentPeriod()), false, false, false, false, false, false);
//			TimesheetSaver userSavedS13=new TimesheetSaver(null, "MAFA013UU", tp.getPrecedentPeriod(tp.getCurrentPeriod()), false, false, false, false, false, false);
//			TimesheetSaver userSavedS14=new TimesheetSaver(null, "MAFA014UU", tp.getPrecedentPeriod(tp.getCurrentPeriod()), false, false, false, false, false, false);
//			TimesheetSaver userSavedS15=new TimesheetSaver(null, "MAFA015UU", tp.getPrecedentPeriod(tp.getCurrentPeriod()), false, false, false, false, false, false);
//			TimesheetSaver userSavedS16=new TimesheetSaver(null, "MAFA016UU", tp.getPrecedentPeriod(tp.getCurrentPeriod()), false, false, false, false, false, false);
//			TimesheetSaver userSavedS17=new TimesheetSaver(null, "MAFA017UU", tp.getPrecedentPeriod(tp.getCurrentPeriod()), false, false, false, false, false, false);
//			TimesheetSaver userSavedS18=new TimesheetSaver(null, "MAFA018UU", tp.getPrecedentPeriod(tp.getCurrentPeriod()), false, false, false, false, false, false);
//			
//			savedSheetLineRepository.save(userSavedSl);
//			savedSheetLineRepository.save(userSavedS2);
//			savedSheetLineRepository.save(userSavedS3);
//			savedSheetLineRepository.save(userSavedS4);
//			savedSheetLineRepository.save(userSavedS5);
//			savedSheetLineRepository.save(userSavedS6);
//			savedSheetLineRepository.save(userSavedS7);
//			savedSheetLineRepository.save(userSavedS8);
//			savedSheetLineRepository.save(userSavedS9);
//			savedSheetLineRepository.save(userSavedS10);
//			savedSheetLineRepository.save(userSavedS11);
//			savedSheetLineRepository.save(userSavedS12);
//			savedSheetLineRepository.save(userSavedS13);
//			savedSheetLineRepository.save(userSavedS14);
//			savedSheetLineRepository.save(userSavedS15);
//			savedSheetLineRepository.save(userSavedS16);
//			savedSheetLineRepository.save(userSavedS17);
//			savedSheetLineRepository.save(userSavedS18);			
//			
		}; 
	}	 
}
