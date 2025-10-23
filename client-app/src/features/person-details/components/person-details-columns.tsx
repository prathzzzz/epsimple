import { ColumnDef } from "@tanstack/react-table";
import type { PersonDetails } from "../api/schema";
import { PersonDetailsRowActions } from "./person-details-row-actions";

export const personDetailsColumns: ColumnDef<PersonDetails>[] = [
  {
    accessorKey: "fullName",
    header: "Full Name",
    cell: ({ row }) => {
      const fullName = row.original.fullName;
      return <div className="font-medium">{fullName || "—"}</div>;
    },
  },
  {
    accessorKey: "personTypeName",
    header: "Person Type",
  },
  {
    accessorKey: "email",
    header: "Email",
  },
  {
    accessorKey: "contactNumber",
    header: "Contact Number",
    cell: ({ row }) => {
      const contactNumber = row.original.contactNumber;
      return <div>{contactNumber || "—"}</div>;
    },
  },
  {
    accessorKey: "permanentAddress",
    header: "Permanent Address",
    cell: ({ row }) => {
      const address = row.original.permanentAddress;
      return (
        <div className="max-w-[300px] truncate">
          {address || "—"}
        </div>
      );
    },
  },
  {
    id: "actions",
    cell: ({ row }) => <PersonDetailsRowActions row={row} />,
  },
];
