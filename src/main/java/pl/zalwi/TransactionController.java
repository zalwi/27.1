package pl.zalwi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.zalwi.data.Transaction;
import pl.zalwi.data.TransactionType;
import pl.zalwi.data.TransactionsRepository;
import pl.zalwi.logic.TransactionDao;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class TransactionController {

    private TransactionsRepository transactionsRepository;
    private TransactionDao transactionDao;

    @Autowired
    public TransactionController(TransactionsRepository transactionsRepository, TransactionDao transactionDao) {
        this.transactionsRepository = transactionsRepository;
        this.transactionDao = transactionDao;
    }

    @GetMapping("/")
    public String main(Model model) {
        model.addAttribute("expense", TransactionType.EXPENSE);
        model.addAttribute("income", TransactionType.INCOME);
        return "home";
    }

    @GetMapping("/list")
    public String listTransactions(@RequestParam(required = false, name = "type") Optional<TransactionType> optionalTransactionType, Model model) {

        if(optionalTransactionType.isEmpty()){
            model.addAttribute("listDescription", "wszystkie");
            Optional<List<Transaction>> optionalTransactions = transactionDao.readList(null);
            if(optionalTransactions.isPresent()){
                model.addAttribute("items", optionalTransactions.get());
            }else{
                model.addAttribute("items", null);
            }
            return "list";
        }else{
            TransactionType transactionType= optionalTransactionType.get();
            Optional<List<Transaction>> optionalTransactions = transactionDao.readList(transactionType);
            if(optionalTransactions.isPresent()){
                model.addAttribute("items", optionalTransactions.get());
            }else{
                model.addAttribute("items", null);
            }
            model.addAttribute("listDescription", transactionType.getDescription());
            return "list";
        }
    }

    @GetMapping("/new")
    public String newTransaction(Model model) {
        model.addAttribute("actionDescription", "Dodawanie transakcji");
        model.addAttribute("action", "Dodaj");
        model.addAttribute("actionLink", "add");
        model.addAttribute("types", TransactionType.values());
        model.addAttribute("isIdBlocked", true);
        model.addAttribute("id", null);
        model.addAttribute("isBlocked", false);
        return "transactionForm";
    }

    @GetMapping("/update")
    public String updateTransaction(@RequestParam(name = "id") Long id, Model model) {

//        Transaction transaction = new Transaction(3L,
//                TransactionType.EXPENSE,
//                "Wędka",
//                new BigDecimal("359"),
//                ZonedDateTime.of(2020, 7, 9, 8, 45, 42, 0, ZoneId.systemDefault()
//                ));
        Transaction transaction;
        Optional<Transaction> optionalTransaction = transactionDao.read(id);
        if(optionalTransaction.isPresent()) {
            transaction = optionalTransaction.get();
        }else{
            return "err";
        }

        model.addAttribute("actionDescription", "Modyfikowanie transakcji");
        model.addAttribute("action", "Modyfikuj");
        model.addAttribute("actionLink", "modify");
        model.addAttribute("isIdBlocked", true);
        model.addAttribute("id", transaction.getId());
        model.addAttribute("oldType", transaction.getType());
        model.addAttribute("types", TransactionType.values());
        model.addAttribute("description", transaction.getDescription());
        model.addAttribute("amount", transaction.getAmount());
        model.addAttribute("datetime", transaction.htmlDateTimeFormat());
        model.addAttribute("isBlocked", false);
        return "transactionForm";
    }

    @GetMapping("/remove")
    public String removeTransaction(@RequestParam(name = "id") Long id, Model model) {

//        Transaction transaction = new Transaction(3L,
//                TransactionType.EXPENSE,
//                "Wędka",
//                new BigDecimal("359"),
//                ZonedDateTime.of(2020, 7, 9, 8, 45, 42, 0, ZoneId.systemDefault()
//                ));
        Transaction transaction;
        Optional<Transaction> optionalTransaction = transactionDao.read(id);
        if(optionalTransaction.isPresent()) {
            transaction = optionalTransaction.get();
        }else{
            return "err";
        }
        model.addAttribute("actionDescription", "Usuwanie transakcji");
        model.addAttribute("action", "Usuń");
        model.addAttribute("actionLink", "delete");
        model.addAttribute("isIdBlocked", true);
        model.addAttribute("id", transaction.getId());
        model.addAttribute("oldType", transaction.getType());
        model.addAttribute("types", TransactionType.values());
        model.addAttribute("description", transaction.getDescription());
        model.addAttribute("amount", transaction.getAmount());
        model.addAttribute("datetime", transaction.htmlDateTimeFormat());
        model.addAttribute("isBlocked", true);
        return "transactionForm";
    }

    @PostMapping("/add")
    public String addTransaction(   @RequestParam(name = "type") TransactionType type,
                                    @RequestParam(name = "description")  String description,
                                    @RequestParam(name = "amount")  BigDecimal amount,
                                    @RequestParam(name = "datetime")  String dateTime,
                                    Model model) {
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(dateTime+":00+00:00[Europe/Warsaw]");
        Transaction transaction = new Transaction(type, description,amount,zonedDateTime);
        //System.out.println("Dodawanie: " + transaction);
        transactionDao.create(transaction);
        return "redirect:/list";
    }

    @PostMapping("/modify")
    public String modifyTransaction(@RequestParam(name = "id") Long id,
                                    @RequestParam(name = "type") TransactionType type,
                                    @RequestParam(name = "description")  String description,
                                    @RequestParam(name = "amount")  BigDecimal amount,
                                    @RequestParam(name = "datetime")  String dateTime,
                                    Model model) {
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(dateTime+":00+00:00[Europe/Warsaw]");
        Transaction transaction = new Transaction(id, type, description,amount,zonedDateTime);
        //System.out.println("Modyfikacja: " + transaction);
        transactionDao.update(transaction);
        return "redirect:/list";
    }

    @PostMapping("/delete")
    public String deleteTransaction(@RequestParam(name = "id") Long id) {
        //System.out.println("Do usunięcia: " + id);
        transactionDao.delete(id);
        return "redirect:/list";
    }

}
