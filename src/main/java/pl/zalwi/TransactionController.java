package pl.zalwi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.zalwi.data.Transaction;
import pl.zalwi.data.TransactionType;
import pl.zalwi.logic.TransactionDao;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Controller
public class TransactionController {

    private TransactionDao transactionDao;

    @Autowired
    public TransactionController(TransactionDao transactionDao) {
        this.transactionDao = transactionDao;
    }

    @GetMapping("/")
    public String main(Model model) {
        model.addAttribute("expense", TransactionType.EXPENSE);
        model.addAttribute("income", TransactionType.INCOME);
        return "home";
    }

    @GetMapping("/list")
    public String listTransactions(@RequestParam(name = "type") Optional<TransactionType> optionalTransactionType, Model model) {
        optionalTransactionType.ifPresentOrElse(
                (transactionType) -> {
                    model.addAttribute("items", transactionDao.findByCategory(transactionType));
                    model.addAttribute("listDescription", transactionType.getDescription());
                },
                () -> {
                    model.addAttribute("items", transactionDao.findAll());
                    model.addAttribute("listDescription", "wszystkie");
                }
        );
        return "list";
    }

    @GetMapping("/new")
    public String newTransaction(Model model) {
        model.addAttribute("actionDescription", "Dodawanie transakcji");
        model.addAttribute("action", "Dodaj");
        model.addAttribute("actionLink", "add");
        model.addAttribute("isBlocked", false);
        return "transactionForm";
    }

    @GetMapping("/update")
    public String updateTransaction(@RequestParam(name = "id") Long id, Model model) {
        Transaction transaction;
        Optional<Transaction> optionalTransaction = transactionDao.read(id);
        if (optionalTransaction.isPresent()) {
            transaction = optionalTransaction.get();
        } else {
            return "err";
        }
        model.addAttribute("actionDescription", "Modyfikowanie transakcji");
        model.addAttribute("action", "Modyfikuj");
        model.addAttribute("actionLink", "modify");
        model.addAttribute("isBlocked", false);
        model.addAttribute("transaction", transaction);
        return "transactionForm";
    }

    @GetMapping("/remove")
    public String removeTransaction(@RequestParam(name = "id") Long id, Model model) {
        Transaction transaction;
        Optional<Transaction> optionalTransaction = transactionDao.read(id);
        if (optionalTransaction.isPresent()) {
            transaction = optionalTransaction.get();
        } else {
            return "err";
        }
        model.addAttribute("actionDescription", "Usuwanie transakcji");
        model.addAttribute("action", "Usuń");
        model.addAttribute("actionLink", "delete");
        model.addAttribute("isBlocked", true);
        model.addAttribute("transaction", transaction);
        return "transactionForm";
    }

    @PostMapping("/add")
    public String addTransaction(@RequestParam TransactionType type,
                                 @RequestParam String description,
                                 @RequestParam BigDecimal amount,
                                 @RequestParam(name = "datetime") String dateTime) {
        Transaction transaction = new Transaction(type, description, amount, LocalDateTime.parse(dateTime));
        transactionDao.create(transaction);
        return "redirect:/list";
    }

    @PostMapping("/modify")
    public String modifyTransaction(@RequestParam Long id,
                                    @RequestParam TransactionType type,
                                    @RequestParam String description,
                                    @RequestParam BigDecimal amount,
                                    @RequestParam(name = "datetime") String dateTime) {
        Transaction transaction = new Transaction(id, type, description, amount, LocalDateTime.parse(dateTime));
        transactionDao.update(transaction);
        return "redirect:/list";
    }

    @PostMapping("/delete")
    public String deleteTransaction(@RequestParam Long id) {
        transactionDao.delete(id);
        return "redirect:/list";
    }
}
