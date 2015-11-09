# About
Tempo is a simple tool that allows users to plan and manage their daily schedules using single line commands. With Tempo, you can now add, edit, search, delete, and view events and tasks - all with a single line command!

# Table of Contents
- [Feature Details](#feature-details)
  - [Basic Operations](#basic-operations)
  - [Input Formats](#input-formats)
  - [Manage Your Events](#manage-your-events)
	  - [Add Event] (#add-event)
	  - [Update Event] (#update-event)
	  - [Delete Event] (#delete-event)
  - [Manage Your Tasks](#manage-your-tasks)
	  - [Add Task] (#add-task)
	  - [Update Task] (#update-task)
	  - [Mark Task as Done/Undone] (#mark-task-as-done-undone)
	  - [Delete Task] (#delete-task)
  - [Manage Recurring Items](#manage-recurring-items)
	  - [Add Recurring Item] (#add-recurring-item)
	  - [Repeat Item] (#repeat-item)
	  - [Change Recurring Type] (#change-recurring-type)
	  - [Update Recurring Item] (#update-recurring-item)
	  - [Delete Recurring Item] (#delete-recurring-item)
  - [Display Calendar](#display-calendar)
  - [Search](#search)
- [Cheatsheet](#cheatsheet)

# Feature Details
## Basic Operations
To quickly list available commands, use the command `help`. 

To undo your previous operation, use the command `undo`. The most recently made change will then be reverted. Multiple undo's are supported.

To redo an undo operation, use the command `redo`. Note that the redo operation can only be used after an undo command has been used.

To clear the file in which your schedule is stored, use the command `clear`.

To obtain the id of your events or tasks, use any view or search command listed below. The ids of your events or tasks are unique for each item and necessary for their updating and deletion.

To exit Tempo, use the command `exit`.

## Input Formats
For dates, Tempo supports the formats `DD/MM/YYYY` (default), `yesterday`, `today` and `tomorrow`. If the date input does not exist, you will be alerted and the previous operation with the invalid date input will not be carried out.

The input format for time across all commands should follow the 24-hour format (e.g. 2:01PM should be input as `1401`). The AM/PM format is allowed for whole numbers (e.g. `2pm	`, `11am`).

## Manage Your Events
### Add Event
To add an event, use the following commands. The key `add` can be replaced by `create` or `new`.

Different start and end dates:

    `add event <name> from <start date> at <start time> to <end date> at <end time>`

Same start and end date:

    `add event <name> from <start date> at <start time> to <end time>` 

### Update Event
To update an event, use the following command. The key `update` can be replaced by `edit` or `change`. If there are more than one fields to be modified, append as many `<field name>:<new value>` as required using the delimiter `; `. 

 - `update <id> <field name>:<new value>`
 - `update <id> <field name>:<new value>; <field name>:<new value>`

### Delete Event
To delete an event, use the following command. The key `delete` can be replaced by `cancel` or `remove`. If you delete a recurring event, all instances of the event will be deleted.

    delete <id>

## Manage Your Tasks
### Add Task
To add a task, use one of the following commands. The key `add` can be replaced by `create` or `new`. Use the second command if a deadline is to be specified.

 - `add task <name>`
 - `add task <name> due <date>`

### Update Task
To update a task, use the following command. The key `update` can be replaced by `edit` or `change`. If there are more than one fields to be modified, append as many `<field name>:<new value>` as required using the delimeter `; `. 

 - `update <id> <field name>:<new value>`
 - `update <id> <field name>:<new value>; <field name>:<new value>`

### Mark Task as Done/Undone
To mark a task as done or undone, use the following commands. The key `done` can be replaced by `finished` or `completed`.

 - `done <id>` 
 - `undone<id>`

### Delete Task
To delete a task, use the following command. The key `delete` can be replaced by `cancel` or `remove`. If you delete a recurring event, all instances of the event will be deleted.

    delete <id>

##Manage Recurring Items
### Add Recurring Item
To add recurring events/tasks, use the following commands, where `<frequency>` is `daily`, `weekly`, `monthly` or `yearly`. Note that floating tasks cannot be recurred. 

Repeat for 20 times (default) after current event/task (replace `event` with `task`):

    `add event <name> from <start date> at <start time> to <end date> at <end time> repeat <frequency>`

Repeat until specified end date (replace `event` with `task`):

    add event <name> from start date> at <start time> to <end date> at <end time> repeat <frequency> until <end recurring date>

###Repeat Item
To make an existing event/task recurring, use the following update command. 

    update <id> repeat:<frequency>

###Change Recurring Type
To change the recurring type of a recurring event/task, use the following update command.

update all <id> repeat:<new frequency>

###Update Recurring Item
To update an instance of the recurring series, use the normal update command as listed above. To update all the instances of the recurring series, use the follow command.

    update all <id> <field>:<new value>

###Delete Recurring Item
To delete an instance of the recurring series, use the normal delete command as listed above. To delete all the instances of the recurring series, use the following command.

    delete all <id>

## Display Calendar
To view the events and tasks that are happening or due today, use the following command. The key `view` can be replaced by `display`.

    view today

To view all events and tasks, use the following command. The key `view` can be replaced by `display`.

    view all

To view only your events, use the following commands. The key `view` can be replaced by `display`.

 - `view upcoming events`
 - `view events`

To view only your tasks, use the following command. The key `view` can be replaced by `display`.

 - `view undone tasks`
 - `view missed tasks`
 - `view tasks`


## Search 
To search for an event or task, use the following commands. At least part of the event or task name must be keyed in the `<keywords>` field. The key `search` can be replaced by `find`.

    search <keywords>

# Cheatsheet
Command | `<key>` Variations| Description
--------| --------------| ---------------
`help` | N.A. | List available commands
`<key> event <name> from <start date> at <start time> to <end date> at <end time>` | `add`, `create`, `new` | Add event
`<key> task <name>` | `add`, `create`, `new` | Add floating task (no deadline)
`<key> task <name> due <date>` | `add`, `create`, `new` | Add task (with deadline)
`<key> event <name> from <start date> at <start time> to <end date> at <end time> repeat <frequency>` | `add`, `create`, `new` | Add recurring event/task (replace `event` with `task`) (no end date specified)
`<key> event <name> from <start date> at <start time> to <end date> at <end time> repeat <frequency> till <end recurring date>` | `add`, `create`, `new` | Add recurring event/task (replace `event` with `task`) (with end date specified)
`<key> <id> <field name>:<new value>` | `update`, `edit`, `change` | Update single field of event/task
`<key> all <id> <field name>:<new value>` | `update`, `edit`, `change` | Update single field of event/task for whole recurring series
`<key> <id> <field name>:<new value>; <field name>:<new value>` | `update`, `edit`, `change` | Update multiple fields of event/task
`<key> all <id> <field name>:<new value>; <field name>:<new value>` | `update`, `edit`, `change` | Update multiple fields of event/task for whole recurring series
`<key> <id> repeat:<frequency>` | `update`, `edit`, `change` | Repeat existing event (daily, weekly, monthly or yearly)
`<key> all <id> repeat:<frequency>` | `update`, `edit`, `change` | Change recurring type (daily, weekly, monthly or yearly)
`<key> <id>` | `done`, `finished`, `completed` | Mark task as done 
`<key> <id>` | `undone` | Mark task as undone 
`<key> <id>` | `delete`, `remove`, `cancel` | Delete event or task
`<key> all <id>` | `delete`, `remove`, `cancel` | Delete event or task in whole recurring series
`<key> today` | `view`, `display` | View today's events and tasks
`<key> all` | `view`, `display` | View all events and tasks
`<key> upcoming events` | `view`, `display` | View upcoming events
`<key> events` | `view`, `display` | View all events
`<key> undone tasks` | `view`, `display` | View undone tasks
`<key> missed tasks` | `view`, `display` | View missed events
`<key> tasks` | `view`, `display` | View all tasks
`<key> <keywords>` | `search`, `find` | Search for an event or task
`undo` | N.A. | Undo previous command
`redo` | N.A. | Redo previous undo command
`clear` | N.A. | Clear calendar
`exit` | N.A. | Exit Tempo



