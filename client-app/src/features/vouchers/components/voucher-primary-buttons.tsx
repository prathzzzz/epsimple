import { Plus } from "lucide-react";
import { Button } from "@/components/ui/button";
import { useVoucher } from "../context/voucher-provider";

export function VoucherPrimaryButtons() {
  const { setIsDrawerOpen, setEditingVoucher } = useVoucher();

  const handleAdd = () => {
    setEditingVoucher(null);
    setIsDrawerOpen(true);
  };

  return (
    <Button onClick={handleAdd}>
      <Plus className="mr-2 h-4 w-4" />
      Add Voucher
    </Button>
  );
}
